package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductVm {
    @NotBlank
    private String productName;

    private String productDescription;

    private double productPrice;

    private int stockAmount;

    // Product da sadece category instance oluşturduk
    private int categoryId;
}
