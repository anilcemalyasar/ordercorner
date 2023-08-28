package com.btk.ordercorner.model.dto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @NotNull
    private int customerId;

    @NotBlank
    @Size(max = 100)
    private String customerFirstName;

    @NotBlank
    @Size(max = 100)
    private String customerLastName;

    @NotBlank
    @Size(max = 50)
    private String username;

    @Size(max = 100)
    private String mailAddress;

    @NotBlank
    private String password;

    private double wallet;
    
    @NotBlank
    private String roles;

    private List<OrderDto> orders;
}
