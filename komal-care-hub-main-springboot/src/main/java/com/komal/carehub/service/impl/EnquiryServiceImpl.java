package com.komal.carehub.service.impl;

import com.komal.carehub.dto.EnquiryDto;
import com.komal.carehub.dto.EnquiryItemDto;
import com.komal.carehub.dto.EntityMapper;
import com.komal.carehub.entity.Enquiry;
import com.komal.carehub.entity.EnquiryItem;
import com.komal.carehub.entity.Product;
import com.komal.carehub.entity.User;
import com.komal.carehub.entity.enums.EnquiryStatus;
import com.komal.carehub.exception.ResourceNotFoundException;
import com.komal.carehub.repository.EnquiryRepository;
import com.komal.carehub.repository.ProductRepository;
import com.komal.carehub.repository.UserRepository;
import com.komal.carehub.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EntityMapper mapper;

    @Override
    public EnquiryDto createEnquiry(EnquiryDto enquiryDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Enquiry enquiry = new Enquiry();
        enquiry.setUser(user);
        enquiry.setStatus(EnquiryStatus.PENDING);

        if (enquiryDto.getItems() != null) {
            List<EnquiryItem> items = enquiryDto.getItems().stream().map(itemDto -> {
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                EnquiryItem item = new EnquiryItem();
                item.setProduct(product);
                item.setQuantity(itemDto.getQuantity());
                item.setPriceAtTimeOfEnquiry(itemDto.getPrice() != null ? itemDto.getPrice() : product.getPrice());
                item.setEnquiry(enquiry);
                return item;
            }).collect(Collectors.toList());
            enquiry.setItems(items);
        }

        Enquiry savedEnquiry = enquiryRepository.save(enquiry);
        return mapper.toEnquiryDto(savedEnquiry);
    }

    @Override
    public List<EnquiryDto> getMyEnquiries(Long userId) {
        return enquiryRepository.findByUserId(userId).stream()
                .map(mapper::toEnquiryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnquiryDto> getMyOwnEnquiries() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return getMyEnquiries(user.getId());
    }

    @Override
    public List<EnquiryDto> getAllEnquiries() {
        return enquiryRepository.findAll().stream()
                .map(mapper::toEnquiryDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnquiryDto updateEnquiryStatus(Long id, String status) {
        Enquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found"));
        enquiry.setStatus(EnquiryStatus.valueOf(status.toUpperCase()));
        return mapper.toEnquiryDto(enquiryRepository.save(enquiry));
    }
}

