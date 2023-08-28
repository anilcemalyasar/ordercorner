package com.btk.ordercorner.model.dto;

import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.model.entity.Product;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {
    
    @NotNull
    private int cartId;
    @NotNull
    private int customerId;
    @NotNull
    private int productId;
    private int quantity;  
    private double totalAmount;
}
