package com.komal.carehub.service;

import com.komal.carehub.dto.EnquiryDto;
import java.util.List;

public interface EnquiryService {
    EnquiryDto createEnquiry(EnquiryDto enquiryDto);
    List<EnquiryDto> getMyEnquiries(Long userId);
    List<EnquiryDto> getMyOwnEnquiries();
    List<EnquiryDto> getAllEnquiries();
    EnquiryDto updateEnquiryStatus(Long id, String status);
}

