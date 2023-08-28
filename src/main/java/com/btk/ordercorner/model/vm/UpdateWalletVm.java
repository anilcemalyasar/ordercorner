package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWalletVm {
    
    @NotNull
    private int customerId;

    @Min(value = 0)
    private double wallet;
    
}
