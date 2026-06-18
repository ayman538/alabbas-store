package com.alabbas.store.entity;


import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product English name is required")
    @Size(max = 150, message = "Product English name must not exceed 150 characters")
    @Column(name = "name_en", length = 150, nullable = false)
    private String nameEn;

    @NotBlank(message = "Product Arabic name is required")
    @Size(max = 150, message = "Product Arabic name must not exceed 150 characters")
    @Column(name = "name_ar", length = 150, nullable = false)
    private String nameAr;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Store price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Store price must be greater than 0")
    @Column(nullable = false)
    private BigDecimal storePrice;


    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(nullable = false)
    private Integer stockQuantity;

    private Boolean published = false;

    @Column(name = "company_en", length = 150)
    private String companyEn;

    @Column(name = "company_ar", length = 150)
    private String companyAr;

    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String sku;


    private String imageUrl;
}
