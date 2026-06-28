package com.alabbas.store.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank
    private String sku;

    @NotBlank
    private String nameEn;

    @NotBlank
    private String nameAr;

    private String description;
    private String descriptionAr;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal storePrice;


    private String imageUrl;

    @NotNull
    private Integer stockQuantity;

    private Boolean published;

    private String companyEn;
    private String companyAr;

    @NotNull
    private Long categoryId;
}