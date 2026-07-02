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

    @NotBlank(message = "{product.nameEn.required}")
    @Size(max = 150, message = "{product.nameEn.size}")
    @Column(name = "name_en", length = 150, nullable = false)
    private String nameEn;

    @NotBlank(message = "{product.nameAr.required}")
    @Size(max = 150, message = "{product.nameAr.size}")
    @Column(name = "name_ar", length = 150, nullable = false)
    private String nameAr;

    @Size(max = 1000, message = "{product.description.size}")
    @Column(length = 1000)
    private String description;

    @Size(max = 1000, message = "{product.descriptionAr.size}")
    @Column(length = 1000)
    private String descriptionAr;


    @NotNull(message = "{product.price.required}")
    @Column(nullable = false)
    private BigDecimal price;


    @NotNull(message = "{product.storePrice.required}")
    @Column(nullable = false)
    private BigDecimal storePrice;


    @NotNull(message = "{product.stockQuantity.required}")
    @Column(nullable = false)
    private Integer stockQuantity;

    private Boolean published = false;

    @Column(name = "company_en", length = 150)
    private String companyEn;

    @Column(name = "company_ar", length = 150)
    private String companyAr;

    @NotNull(message = "{product.category.required}")
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

    @Size(max = 50, message = "{product.sku.size}")
    @Column(unique = true, length = 50)
    private String sku;


    private String imageUrl;
}
