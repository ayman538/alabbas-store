package com.alabbas.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedProductsSummaryResponse {

    private String transactionId;
    private BigDecimal totalAmount;
    private Long totalQuantity;
    private Long itemsCount;
    private LocalDateTime createdAt;
}
