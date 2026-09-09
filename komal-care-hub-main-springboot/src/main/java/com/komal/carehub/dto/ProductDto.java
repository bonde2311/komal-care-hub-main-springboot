package com.komal.carehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String stockStatus;
    private Long categoryId;
    private String imageUrl;
    private String imageId;
    private Boolean isActive;
    
    // Inventory
    private Integer stockQuantity;
    private Integer reorderLevel;
    private String batchNumber;
    private java.time.LocalDate expiryDate;
    private Boolean prescriptionRequired;

    // Pricing
    private BigDecimal mrp;
    private BigDecimal discountPercentage;
    private BigDecimal purchasePrice;
    private BigDecimal gstPercentage;

    // Medical
    private String manufacturer;
    private String composition;
    private String packagingType;
}

