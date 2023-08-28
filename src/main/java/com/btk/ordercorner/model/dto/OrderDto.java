package com.btk.ordercorner.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private int orderId;
    
    private Date orderDate;
    private boolean orderStatus;
    private double totalAmount;

    @NotNull
    private int paymentId;
    @NotNull
    private int customerId;
    @NotNull
    private int cartId;
}
