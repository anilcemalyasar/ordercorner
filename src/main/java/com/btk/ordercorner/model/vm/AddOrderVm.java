package com.btk.ordercorner.model.vm;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderVm {

    @JsonFormat(pattern="dd-MM-YYYY")
    private Date orderDate;

    private boolean orderStatus;

    private int paymentId;

    private int customerId;

    private int cartId;


    
}
