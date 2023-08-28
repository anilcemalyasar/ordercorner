package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddListOfProductsInCartVm {
    @NotNull
    private int productId;
    private double productPrice;
    private int quantity;
}
