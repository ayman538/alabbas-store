package com.alabbas.store.dto;
import com.alabbas.store.enums.OrderItemStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private OrderItemStatus status;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}