package com.btk.ordercorner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotNull
    private int productId;

    @NotBlank
    private String productName;

    private String productDescription;

    private double productPrice;

    private int stockAmount;

    // Product da sadece category instance oluşturduk
    private int categoryId;

}
