package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderDetailsVm {
    
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
