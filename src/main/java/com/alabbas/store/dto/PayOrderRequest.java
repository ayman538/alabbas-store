package com.alabbas.store.dto;

import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayOrderRequest {

    @NotNull(message = "Paid amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Paid amount must be greater than 0")
    private BigDecimal paidAmount;
    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

}