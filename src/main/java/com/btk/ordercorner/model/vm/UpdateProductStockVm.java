package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductStockVm {
    
    @NotNull
    private int productId;

    private int stockAmount;
}
