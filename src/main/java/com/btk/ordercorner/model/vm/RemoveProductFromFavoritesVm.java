package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveProductFromFavoritesVm {
    
    private String productName;

}
