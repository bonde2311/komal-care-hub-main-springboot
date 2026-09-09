
package com.komal.carehub.service.impl;

import com.komal.carehub.dto.EntityMapper;
import com.komal.carehub.dto.ProductDto;
import com.komal.carehub.entity.Category;
import com.komal.carehub.entity.Product;
import com.komal.carehub.entity.enums.StockStatus;
import com.komal.carehub.exception.ResourceNotFoundException;
import com.komal.carehub.repository.CategoryRepository;
import com.komal.carehub.repository.ProductRepository;
import com.komal.carehub.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.komal.carehub.service.CloudinaryService;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final EntityMapper mapper;

    @Override
    public ProductDto createProduct(ProductDto productDto, MultipartFile image) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = mapper.toProduct(productDto);
        product.setCategory(category);
        if (productDto.getStockStatus() != null) {
            product.setStockStatus(StockStatus.valueOf(productDto.getStockStatus()));
        }
        
        if (product.getStockQuantity() != null && product.getStockQuantity() <= 0) {
            product.setStockStatus(StockStatus.OUT_OF_STOCK);
        }
        
        if (image != null && !image.isEmpty()) {
            Map<String, Object> uploadResult = cloudinaryService.upload(image);
            product.setImageUrl(uploadResult.get("url").toString());
            product.setImageId(uploadResult.get("public_id").toString());
        }
        
        return mapper.toProductDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile image) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        if (productDto.getStockStatus() != null) {
            product.setStockStatus(StockStatus.valueOf(productDto.getStockStatus()));
        }
        
        // Inventory
        product.setStockQuantity(productDto.getStockQuantity());
        product.setReorderLevel(productDto.getReorderLevel());
        product.setBatchNumber(productDto.getBatchNumber());
        product.setExpiryDate(productDto.getExpiryDate());
        product.setPrescriptionRequired(productDto.getPrescriptionRequired());

        // Pricing
        product.setMrp(productDto.getMrp());
        product.setDiscountPercentage(productDto.getDiscountPercentage());
        product.setPurchasePrice(productDto.getPurchasePrice());
        product.setGstPercentage(productDto.getGstPercentage());

        // Medical
        product.setManufacturer(productDto.getManufacturer());
        product.setComposition(productDto.getComposition());
        product.setPackagingType(productDto.getPackagingType());

        if (product.getStockQuantity() != null && product.getStockQuantity() <= 0) {
            product.setStockStatus(StockStatus.OUT_OF_STOCK);
        }
        
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        
        if (image != null && !image.isEmpty()) {
            // Delete old image from cloudinary if it exists
            if (product.getImageId() != null) {
                cloudinaryService.delete(product.getImageId());
            }
            Map<String, Object> uploadResult = cloudinaryService.upload(image);
            product.setImageUrl(uploadResult.get("url").toString());
            product.setImageId(uploadResult.get("public_id").toString());
        }
        
        return mapper.toProductDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(mapper::toProductDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(mapper::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProducts(String q) {
        return productRepository.findTop50ByNameContainingIgnoreCase(q).stream()
                .map(mapper::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<ProductDto> searchProductsPaginated(String q, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(q, pageable)
                .map(mapper::toProductDto);
    }

    @Override
    public ProductDto toggleActive(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setIsActive(product.getIsActive() == null || !product.getIsActive());
        return mapper.toProductDto(productRepository.save(product));
    }

    @Override
    public List<ProductDto> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(mapper::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getExpiringProducts(int daysThreshold) {
        java.time.LocalDate targetDate = java.time.LocalDate.now().plusDays(daysThreshold);
        return productRepository.findExpiringProducts(targetDate).stream()
                .map(mapper::toProductDto)
                .collect(Collectors.toList());
    }
}

