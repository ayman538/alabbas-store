package com.alabbas.store.repository;

import com.alabbas.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsByNameEnIgnoreCase(String nameEn);

    boolean existsByNameAr(String nameAr);

    Page<Product> findByCategoryId(Long categoryId,Pageable pageable);

    @Query("""
                SELECT p FROM Product p
                WHERE LOWER(p.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.nameAr) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.category.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.category.nameAr) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
                SELECT p FROM Product p
                WHERE p.category.id = :categoryId
                  AND (
                       LOWER(p.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(p.nameAr) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
            """)
    Page<Product> searchProductsInCategory(@Param("keyword") String keyword,
                                           @Param("categoryId") Long categoryId,
                                           Pageable pageable);
}