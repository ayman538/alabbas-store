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


    private String sku;

    @NotBlank(message = "{product.nameEn.required}")

    private String nameEn;

    @NotBlank(message = "{product.nameAr.required}")
    private String nameAr;

    private String description;
    private String descriptionAr;

    @NotNull(message = "{product.price.required}")
    private BigDecimal price;

    @NotNull(message = "{product.storePrice.required}")
    private BigDecimal storePrice;


    private String imageUrl;

    @NotNull(message = "{product.stockQuantity.required}")

    private Integer stockQuantity;

    private Boolean published;

    private String companyEn;
    private String companyAr;

    @NotNull(message = "{product.category.required}")
    private Long categoryId;
}