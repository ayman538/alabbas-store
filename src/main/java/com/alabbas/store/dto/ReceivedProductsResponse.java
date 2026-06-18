package com.alabbas.store.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedProductsResponse {


    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private Integer quantity;
    private String nameEn;
    private String nameAr;
    private LocalDateTime createdAt;
}
