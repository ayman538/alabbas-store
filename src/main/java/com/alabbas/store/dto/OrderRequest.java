package com.alabbas.store.dto;
import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private String email;

    @NotBlank
    private String area;

    @NotBlank
    private String buildingNumber;

    private String floor;

    private String apartment;

    @NotBlank
    private String address;

    private String notes;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private BigDecimal paidAmount;

    @NotEmpty
    private List<OrderItemRequest> items;
}