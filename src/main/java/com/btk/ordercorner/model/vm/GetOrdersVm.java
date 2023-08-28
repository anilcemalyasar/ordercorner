package com.btk.ordercorner.model.vm;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersVm {

    @NotNull
    private int orderId;
    
    private Date orderDate;

    private boolean orderStatus;
    
    private double totalAmount;

    private int paymentId;

    private int customerId;

    private int cartId;
}
