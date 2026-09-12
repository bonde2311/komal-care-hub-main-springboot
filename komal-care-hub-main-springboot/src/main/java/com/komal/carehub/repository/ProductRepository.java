package com.komal.carehub.repository;

import com.komal.carehub.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.List<Product> findByIsActiveTrue();
    java.util.List<Product> findTop50ByNameContainingIgnoreCase(String name);
    org.springframework.data.domain.Page<Product> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT p.name FROM Product p")
    java.util.Set<String> findAllNames();

    @org.springframework.data.jpa.repository.Query("SELECT p.externalId FROM Product p WHERE p.externalId IS NOT NULL")
    java.util.Set<String> findAllExternalIds();

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity IS NOT NULL AND p.reorderLevel IS NOT NULL AND p.stockQuantity <= p.reorderLevel")
    java.util.List<Product> findLowStockProducts();

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Product p WHERE p.isActive = true AND p.expiryDate IS NOT NULL AND p.expiryDate <= :targetDate")
    java.util.List<Product> findExpiringProducts(@org.springframework.data.repository.query.Param("targetDate") java.time.LocalDate targetDate);
}

