package com.komal.carehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String name;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String profilePicture;
}