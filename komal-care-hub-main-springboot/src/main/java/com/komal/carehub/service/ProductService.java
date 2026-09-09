package com.komal.carehub.service;

import com.komal.carehub.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto, MultipartFile image);
    ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile image);
    void deleteProduct(Long id);
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    List<ProductDto> searchProducts(String q);
    org.springframework.data.domain.Page<ProductDto> searchProductsPaginated(String q, int page, int size);
    ProductDto toggleActive(Long id);
    List<ProductDto> getLowStockProducts();
    List<ProductDto> getExpiringProducts(int daysThreshold);
}

