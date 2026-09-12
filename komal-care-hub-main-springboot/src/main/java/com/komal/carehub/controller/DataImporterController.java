package com.komal.carehub.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.komal.carehub.entity.Category;
import com.komal.carehub.entity.Product;
import com.komal.carehub.repository.CategoryRepository;
import com.komal.carehub.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/data")
@RequiredArgsConstructor
@Slf4j
public class DataImporterController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/import-medicines")
    public ResponseEntity<String> importMedicines() {
        try {
            org.springframework.core.io.Resource resource = new org.springframework.core.io.ClassPathResource("data/indian_medicine_data.zip");
            if (!resource.exists()) {
                return ResponseEntity.badRequest().body("File not found: data/indian_medicine_data.zip in resources");
            }

            log.info("Starting import from classpath:data/indian_medicine_data.zip");

            // Prevent duplicate imports by tracking existing IDs and names
            java.util.Set<String> existingNames = productRepository.findAllNames();
            java.util.Set<String> existingExternalIds = productRepository.findAllExternalIds();
            log.info("Found {} existing products in database.", existingNames.size());

            // Check if default category exists, create if not
            Category defaultCategory = categoryRepository.findAll().stream()
                    .filter(c -> c.getName().equalsIgnoreCase("Medicines"))
                    .findFirst()
                    .orElseGet(() -> {
                        Category c = new Category();
                        c.setName("Medicines");
                        c.setDescription("All imported medicines");
                        return categoryRepository.save(c);
                    });

            int count = 0;
            int skipped = 0;
            List<Product> batch = new ArrayList<>();

            try (java.io.InputStream is = resource.getInputStream();
                 java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(is)) {
                 
                java.util.zip.ZipEntry entry = zis.getNextEntry();
                if (entry == null) {
                    return ResponseEntity.badRequest().body("No JSON file found inside the ZIP archive");
                }
                
                log.info("Extracting and parsing {} in a streaming fashion...", entry.getName());
                
                JsonFactory factory = new JsonFactory();
                try (JsonParser parser = factory.createParser(zis)) {
                    if (parser.nextToken() != JsonToken.START_ARRAY) {
                        return ResponseEntity.badRequest().body("Expected content to be an array");
                    }
                    
                    while (parser.nextToken() == JsonToken.START_OBJECT) {
                        Map<String, String> med = objectMapper.readValue(parser, new TypeReference<Map<String, String>>() {});
                        
                        try {
                            String externalId = String.valueOf(med.get("id"));
                            String packSize = med.get("pack_size_label");
                            String productName = med.get("name") + (packSize != null && !packSize.isEmpty() ? " (" + packSize + ")" : "");
                            
                            // Skip if externalId or name is already in the database
                            if (existingNames.contains(productName) || existingExternalIds.contains(externalId)) {
                                skipped++;
                                continue;
                            }

                            Product p = new Product();
                            p.setExternalId(externalId);
                            p.setName(productName);
                            
                            existingNames.add(productName);
                            existingExternalIds.add(externalId);
                            
                            String comp1 = med.get("short_composition1") != null ? med.get("short_composition1").trim() : "";
                            String comp2 = med.get("short_composition2") != null ? med.get("short_composition2").trim() : "";
                            String mfr = med.get("manufacturer_name") != null ? med.get("manufacturer_name").trim() : "";
                            
                            String fullComposition = (comp1 + " " + comp2).trim();
                            p.setDescription(fullComposition + " | Manufacturer: " + mfr);
                            p.setComposition(fullComposition);
                            p.setManufacturer(mfr);
                            p.setPackagingType(packSize);
                            
                            String priceStr = med.get("price(₹)");
                            if (priceStr == null || priceStr.isEmpty() || priceStr.equalsIgnoreCase("nan")) {
                                priceStr = "0.00";
                            }
                            try {
                                p.setPrice(new BigDecimal(priceStr));
                            } catch (Exception e) {
                                p.setPrice(BigDecimal.ZERO);
                            }
                            
                            p.setCategory(defaultCategory);
                            p.setStockStatus(com.komal.carehub.entity.enums.StockStatus.IN_STOCK);
                            p.setStockQuantity(100); 
                            
                            String rx = med.get("rx_required");
                            p.setPrescriptionRequired(rx != null && rx.equalsIgnoreCase("yes"));
                            
                            batch.add(p);
                            
                            if (batch.size() >= 1000) {
                                try {
                                    productRepository.saveAll(batch);
                                    count += batch.size();
                                    log.info("Saved batch of 1000. Total saved: {}", count);
                                } catch (Exception e) {
                                    log.error("Failed to save batch: {}", e.getMessage());
                                } finally {
                                    batch.clear();
                                    // Clean up memory aggressively
                                    System.gc();
                                }
                            }
                            
                        } catch (Exception e) {
                            log.error("Failed to parse a product item: {}", e.getMessage());
                        }
                    }
                }
            }
            
            if (!batch.isEmpty()) {
                try {
                    productRepository.saveAll(batch);
                    count += batch.size();
                    log.info("Saved final batch of {}. Total saved: {}", batch.size(), count);
                } catch (Exception e) {
                    log.error("Failed to save final batch: {}", e.getMessage());
                }
            }
            
            return ResponseEntity.ok(String.format("Import successful! Saved %d new medicines. Skipped %d duplicates.", count, skipped));
            
        } catch (Exception e) {
            log.error("Failed to import medicines", e);
            return ResponseEntity.internalServerError().body("Import failed: " + e.getMessage());
        }
    }
}
