package com.komal.carehub.controller;

import com.komal.carehub.dto.EnquiryDto;
import com.komal.carehub.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<EnquiryDto> createEnquiry(@RequestBody EnquiryDto enquiryDto) {
        return ResponseEntity.ok(enquiryService.createEnquiry(enquiryDto));
    }

    @GetMapping
    public ResponseEntity<List<EnquiryDto>> getAllEnquiries() {
        return ResponseEntity.ok(enquiryService.getAllEnquiries());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EnquiryDto>> getMyEnquiries(@PathVariable Long userId) {
        return ResponseEntity.ok(enquiryService.getMyEnquiries(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<EnquiryDto>> getMyOwnEnquiries() {
        return ResponseEntity.ok(enquiryService.getMyOwnEnquiries());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EnquiryDto> updateEnquiryStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(enquiryService.updateEnquiryStatus(id, status));
    }
}

