package com.alabbas.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnOrderItemRequest  {

    @NotNull
    @Min(1)
    private Integer quantity;
}
