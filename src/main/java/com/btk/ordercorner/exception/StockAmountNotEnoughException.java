package com.btk.ordercorner.exception;

public class StockAmountNotEnoughException extends RuntimeException {
    public StockAmountNotEnoughException(String message) {
        super(message);
    }

}
