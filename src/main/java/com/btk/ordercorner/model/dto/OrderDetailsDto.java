package com.btk.ordercorner.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {
    @NotNull
    private int orderId;
    @NotNull
    private int customerId;
    @NotNull
    private int cartId;
    @NotNull
    private int productId;
    
    private int productQuantity;
}
