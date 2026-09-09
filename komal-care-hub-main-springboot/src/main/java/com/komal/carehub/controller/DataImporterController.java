package com.komal.carehub.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.komal.carehub.entity.Category;
import com.komal.carehub.entity.Product;
import com.komal.carehub.entity.enums.StockStatus;
import com.komal.carehub.repository.CategoryRepository;
import com.komal.carehub.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
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
            File file = new File("G:/komal-care-hub/indian_medicine_data.json");
            if (!file.exists()) {
                return ResponseEntity.badRequest().body("File not found: indian_medicine_data.json");
            }

            log.info("Starting import from {}", file.getAbsolutePath());

            // Prevent duplicate imports by tracking existing names
            java.util.Set<String> existingNames = productRepository.findAllNames();
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

            List<Map<String, String>> medicines = objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});
            
            log.info("Parsed {} medicines from JSON. Saving in batches...", medicines.size());

            List<Product> batch = new ArrayList<>();
            int count = 0;
            int skipped = 0;
            
            for (Map<String, String> med : medicines) {
                try {
                    String externalId = String.valueOf(med.get("id"));
                    String productName = med.get("name") + " (" + med.get("pack_size_label") + ")";
                    
                    if (existingNames.contains(productName)) {
                        skipped++;
                        continue;
                    }

                    Product p = new Product();
                    p.setExternalId(externalId);
                    p.setName(productName);
                    
                    // Add to set to prevent duplicates within the same import run
                    existingNames.add(productName);
                    
                    String comp1 = med.get("short_composition1") != null ? med.get("short_composition1").trim() : "";
                    String comp2 = med.get("short_composition2") != null ? med.get("short_composition2").trim() : "";
                    String mfr = med.get("manufacturer_name") != null ? med.get("manufacturer_name").trim() : "";
                    
                    String fullComposition = (comp1 + " " + comp2).trim();
                    p.setDescription(fullComposition + " | Manufacturer: " + mfr);
                    p.setComposition(fullComposition);
                    p.setManufacturer(mfr);
                    p.setPackagingType(med.get("pack_size_label"));
                    
                    String priceStr = med.get("price(₹)");
                    if (priceStr == null || priceStr.isEmpty() || priceStr.equalsIgnoreCase("nan")) {
                        priceStr = "0.00";
                    }
                    try {
                        p.setPrice(new BigDecimal(priceStr));
                    } catch (Exception e) {
                        p.setPrice(BigDecimal.ZERO);
                    }
                    
                    p.setStockStatus(StockStatus.OUT_OF_STOCK);
                    p.setIsActive(false); // Hidden by default
                    p.setCategory(defaultCategory);
                    
                    batch.add(p);
                    count++;
                    
                    if (batch.size() >= 1000) {
                        productRepository.saveAll(batch);
                        batch.clear();
                        log.info("Saved {} medicines so far...", count);
                    }
                } catch (Exception e) {
                    log.error("Failed to parse medicine: " + med.get("name"), e);
                }
            }
            
            if (!batch.isEmpty()) {
                productRepository.saveAll(batch);
            }

            log.info("Successfully imported {} medicines! Skipped {} duplicates.", count, skipped);
            return ResponseEntity.ok("Successfully imported " + count + " medicines! Skipped " + skipped + " duplicates.");

        } catch (Exception e) {
            log.error("Import failed", e);
            return ResponseEntity.internalServerError().body("Import failed: " + e.getMessage());
        }
    }

    @PostMapping("/patch-existing")
    public ResponseEntity<String> patchExistingData() {
        try {
            List<Product> products = productRepository.findAll();
            int count = 0;
            List<Product> batch = new ArrayList<>();
            for (Product p : products) {
                if (p.getDescription() != null && p.getDescription().contains(" | Manufacturer: ")) {
                    String[] parts = p.getDescription().split(" \\| Manufacturer: ");
                    if (p.getComposition() == null || p.getComposition().isEmpty()) {
                        p.setComposition(parts[0].trim());
                    }
                    if (p.getManufacturer() == null || p.getManufacturer().isEmpty()) {
                        if (parts.length > 1) {
                            p.setManufacturer(parts[1].trim());
                        }
                    }
                    batch.add(p);
                    count++;
                }
                
                if (batch.size() >= 1000) {
                    productRepository.saveAll(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                productRepository.saveAll(batch);
            }
            return ResponseEntity.ok("Successfully patched " + count + " existing products with structured medical fields!");
        } catch (Exception e) {
            log.error("Patch failed", e);
            return ResponseEntity.internalServerError().body("Patch failed: " + e.getMessage());
        }
    }
}
