package com.komal.carehub.dto;

import com.komal.carehub.entity.Category;
import com.komal.carehub.entity.Enquiry;
import com.komal.carehub.entity.EnquiryItem;
import com.komal.carehub.entity.Product;
import com.komal.carehub.entity.Review;
import com.komal.carehub.entity.User;
import com.komal.carehub.entity.enums.EnquiryStatus;
import com.komal.carehub.entity.enums.ReviewStatus;
import com.komal.carehub.entity.enums.StockStatus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-09T17:37:50+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public CategoryDto toCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto.CategoryDtoBuilder categoryDto = CategoryDto.builder();

        categoryDto.id( category.getId() );
        categoryDto.name( category.getName() );
        categoryDto.description( category.getDescription() );

        return categoryDto.build();
    }

    @Override
    public Category toCategory(CategoryDto categoryDto) {
        if ( categoryDto == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.id( categoryDto.getId() );
        category.name( categoryDto.getName() );
        category.description( categoryDto.getDescription() );

        return category.build();
    }

    @Override
    public ProductDto toProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.categoryId( productCategoryId( product ) );
        productDto.id( product.getId() );
        productDto.name( product.getName() );
        productDto.description( product.getDescription() );
        productDto.price( product.getPrice() );
        if ( product.getStockStatus() != null ) {
            productDto.stockStatus( product.getStockStatus().name() );
        }
        productDto.imageUrl( product.getImageUrl() );
        productDto.imageId( product.getImageId() );
        productDto.isActive( product.getIsActive() );
        productDto.stockQuantity( product.getStockQuantity() );
        productDto.reorderLevel( product.getReorderLevel() );
        productDto.batchNumber( product.getBatchNumber() );
        productDto.expiryDate( product.getExpiryDate() );
        productDto.prescriptionRequired( product.getPrescriptionRequired() );
        productDto.mrp( product.getMrp() );
        productDto.discountPercentage( product.getDiscountPercentage() );
        productDto.purchasePrice( product.getPurchasePrice() );
        productDto.gstPercentage( product.getGstPercentage() );
        productDto.manufacturer( product.getManufacturer() );
        productDto.composition( product.getComposition() );
        productDto.packagingType( product.getPackagingType() );

        return productDto.build();
    }

    @Override
    public Product toProduct(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.category( productDtoToCategory( productDto ) );
        product.id( productDto.getId() );
        product.name( productDto.getName() );
        product.description( productDto.getDescription() );
        product.price( productDto.getPrice() );
        if ( productDto.getStockStatus() != null ) {
            product.stockStatus( Enum.valueOf( StockStatus.class, productDto.getStockStatus() ) );
        }
        product.imageUrl( productDto.getImageUrl() );
        product.imageId( productDto.getImageId() );
        product.isActive( productDto.getIsActive() );
        product.stockQuantity( productDto.getStockQuantity() );
        product.reorderLevel( productDto.getReorderLevel() );
        product.batchNumber( productDto.getBatchNumber() );
        product.expiryDate( productDto.getExpiryDate() );
        product.prescriptionRequired( productDto.getPrescriptionRequired() );
        product.mrp( productDto.getMrp() );
        product.discountPercentage( productDto.getDiscountPercentage() );
        product.purchasePrice( productDto.getPurchasePrice() );
        product.gstPercentage( productDto.getGstPercentage() );
        product.manufacturer( productDto.getManufacturer() );
        product.composition( productDto.getComposition() );
        product.packagingType( productDto.getPackagingType() );

        return product.build();
    }

    @Override
    public EnquiryDto toEnquiryDto(Enquiry enquiry) {
        if ( enquiry == null ) {
            return null;
        }

        EnquiryDto.EnquiryDtoBuilder enquiryDto = EnquiryDto.builder();

        enquiryDto.userId( enquiryUserId( enquiry ) );
        enquiryDto.userName( enquiryUserName( enquiry ) );
        enquiryDto.userEmail( enquiryUserEmail( enquiry ) );
        enquiryDto.userPhone( enquiryUserPhone( enquiry ) );
        enquiryDto.id( enquiry.getId() );
        if ( enquiry.getStatus() != null ) {
            enquiryDto.status( enquiry.getStatus().name() );
        }
        enquiryDto.items( enquiryItemListToEnquiryItemDtoList( enquiry.getItems() ) );

        return enquiryDto.build();
    }

    @Override
    public Enquiry toEnquiry(EnquiryDto enquiryDto) {
        if ( enquiryDto == null ) {
            return null;
        }

        Enquiry.EnquiryBuilder enquiry = Enquiry.builder();

        enquiry.user( enquiryDtoToUser( enquiryDto ) );
        enquiry.id( enquiryDto.getId() );
        if ( enquiryDto.getStatus() != null ) {
            enquiry.status( Enum.valueOf( EnquiryStatus.class, enquiryDto.getStatus() ) );
        }
        enquiry.items( enquiryItemDtoListToEnquiryItemList( enquiryDto.getItems() ) );

        return enquiry.build();
    }

    @Override
    public EnquiryItemDto toEnquiryItemDto(EnquiryItem enquiryItem) {
        if ( enquiryItem == null ) {
            return null;
        }

        EnquiryItemDto.EnquiryItemDtoBuilder enquiryItemDto = EnquiryItemDto.builder();

        enquiryItemDto.productId( enquiryItemProductId( enquiryItem ) );
        enquiryItemDto.productName( enquiryItemProductName( enquiryItem ) );
        enquiryItemDto.price( enquiryItem.getPriceAtTimeOfEnquiry() );
        enquiryItemDto.quantity( enquiryItem.getQuantity() );

        return enquiryItemDto.build();
    }

    @Override
    public EnquiryItem toEnquiryItem(EnquiryItemDto enquiryItemDto) {
        if ( enquiryItemDto == null ) {
            return null;
        }

        EnquiryItem.EnquiryItemBuilder enquiryItem = EnquiryItem.builder();

        enquiryItem.product( enquiryItemDtoToProduct( enquiryItemDto ) );
        enquiryItem.quantity( enquiryItemDto.getQuantity() );

        return enquiryItem.build();
    }

    @Override
    public ReviewDto toReviewDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto.ReviewDtoBuilder reviewDto = ReviewDto.builder();

        reviewDto.userId( reviewUserId( review ) );
        reviewDto.productId( reviewProductId( review ) );
        reviewDto.id( review.getId() );
        reviewDto.rating( review.getRating() );
        reviewDto.comment( review.getComment() );
        if ( review.getStatus() != null ) {
            reviewDto.status( review.getStatus().name() );
        }

        return reviewDto.build();
    }

    @Override
    public Review toReview(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.user( reviewDtoToUser( reviewDto ) );
        review.product( reviewDtoToProduct( reviewDto ) );
        review.id( reviewDto.getId() );
        review.rating( reviewDto.getRating() );
        review.comment( reviewDto.getComment() );
        if ( reviewDto.getStatus() != null ) {
            review.status( Enum.valueOf( ReviewStatus.class, reviewDto.getStatus() ) );
        }

        return review.build();
    }

    private Long productCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Category productDtoToCategory(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.id( productDto.getCategoryId() );

        return category.build();
    }

    private Long enquiryUserId(Enquiry enquiry) {
        if ( enquiry == null ) {
            return null;
        }
        User user = enquiry.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String enquiryUserName(Enquiry enquiry) {
        if ( enquiry == null ) {
            return null;
        }
        User user = enquiry.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String enquiryUserEmail(Enquiry enquiry) {
        if ( enquiry == null ) {
            return null;
        }
        User user = enquiry.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String enquiryUserPhone(Enquiry enquiry) {
        if ( enquiry == null ) {
            return null;
        }
        User user = enquiry.getUser();
        if ( user == null ) {
            return null;
        }
        String phone = user.getPhone();
        if ( phone == null ) {
            return null;
        }
        return phone;
    }

    protected List<EnquiryItemDto> enquiryItemListToEnquiryItemDtoList(List<EnquiryItem> list) {
        if ( list == null ) {
            return null;
        }

        List<EnquiryItemDto> list1 = new ArrayList<EnquiryItemDto>( list.size() );
        for ( EnquiryItem enquiryItem : list ) {
            list1.add( toEnquiryItemDto( enquiryItem ) );
        }

        return list1;
    }

    protected User enquiryDtoToUser(EnquiryDto enquiryDto) {
        if ( enquiryDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( enquiryDto.getUserId() );

        return user.build();
    }

    protected List<EnquiryItem> enquiryItemDtoListToEnquiryItemList(List<EnquiryItemDto> list) {
        if ( list == null ) {
            return null;
        }

        List<EnquiryItem> list1 = new ArrayList<EnquiryItem>( list.size() );
        for ( EnquiryItemDto enquiryItemDto : list ) {
            list1.add( toEnquiryItem( enquiryItemDto ) );
        }

        return list1;
    }

    private Long enquiryItemProductId(EnquiryItem enquiryItem) {
        if ( enquiryItem == null ) {
            return null;
        }
        Product product = enquiryItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String enquiryItemProductName(EnquiryItem enquiryItem) {
        if ( enquiryItem == null ) {
            return null;
        }
        Product product = enquiryItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected Product enquiryItemDtoToProduct(EnquiryItemDto enquiryItemDto) {
        if ( enquiryItemDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( enquiryItemDto.getProductId() );

        return product.build();
    }

    private Long reviewUserId(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long reviewProductId(Review review) {
        if ( review == null ) {
            return null;
        }
        Product product = review.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected User reviewDtoToUser(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( reviewDto.getUserId() );

        return user.build();
    }

    protected Product reviewDtoToProduct(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( reviewDto.getProductId() );

        return product.build();
    }
}
