package com.btk.ordercorner.model.vm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryVm {
    
    @NotNull
    private int categoryId;
    @NotBlank
    private String categoryName;
}
