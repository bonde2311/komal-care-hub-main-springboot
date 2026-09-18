package com.komal.carehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private Integer rating;
    private String comment;
    private String status;
    private String userName;
    private String userCity;
    private String userImage;
    private Long productId;
    private Long userId;
    private LocalDateTime createdAt;
}

