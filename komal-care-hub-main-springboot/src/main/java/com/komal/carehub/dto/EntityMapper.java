package com.komal.carehub.dto;

import com.komal.carehub.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    CategoryDto toCategoryDto(Category category);
    Category toCategory(CategoryDto categoryDto);

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toProduct(ProductDto productDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.phone", target = "userPhone")
    EnquiryDto toEnquiryDto(Enquiry enquiry);

    @Mapping(source = "userId", target = "user.id")
    Enquiry toEnquiry(EnquiryDto enquiryDto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "priceAtTimeOfEnquiry", target = "price")
    EnquiryItemDto toEnquiryItemDto(EnquiryItem enquiryItem);

    @Mapping(source = "productId", target = "product.id")
    EnquiryItem toEnquiryItem(EnquiryItemDto enquiryItemDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    ReviewDto toReviewDto(Review review);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "productId", target = "product.id")
    Review toReview(ReviewDto reviewDto);
}

