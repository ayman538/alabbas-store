package com.alabbas.store.dto;

import com.alabbas.store.enums.OrderStatus;
import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private String customerName;
    private String customerPhone;
    private String customerArea;
    private String customerAddress;

    private OrderStatus status;
    private BigDecimal totalPrice;
    private String notes;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private BigDecimal discountAmount;

    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}