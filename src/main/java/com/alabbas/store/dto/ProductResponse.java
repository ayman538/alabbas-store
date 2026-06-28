package com.alabbas.store.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String sku;

    private String nameEn;
    private String nameAr;

    private String description;
    private String descriptionAr;

    private BigDecimal price;

    private BigDecimal storePrice;

    private String imageUrl;
    private String uploadedImageUrl;

    private Integer stockQuantity;

    private Boolean published;

    private Long categoryId;
    private String categoryName;
    private String companyEn;
    private String companyAr;


}
