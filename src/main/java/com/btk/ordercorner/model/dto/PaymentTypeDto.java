package com.btk.ordercorner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeDto {

    @NotNull
    private int paymentId;
    
    @NotBlank
    private String paymentName;
}
