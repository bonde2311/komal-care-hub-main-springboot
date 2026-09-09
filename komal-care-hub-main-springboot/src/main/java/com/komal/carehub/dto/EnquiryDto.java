package com.komal.carehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryDto {
    private Long id;
    private String status;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private List<EnquiryItemDto> items;
}

