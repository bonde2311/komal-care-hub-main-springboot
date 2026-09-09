package com.komal.carehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private java.math.BigDecimal price;
}

