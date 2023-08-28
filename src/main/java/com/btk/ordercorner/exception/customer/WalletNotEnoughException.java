package com.btk.ordercorner.exception.customer;

public class WalletNotEnoughException extends RuntimeException {
    public WalletNotEnoughException(){
    }

    public WalletNotEnoughException(String message) {
        super(message);
    }
    public WalletNotEnoughException(Throwable cause) {
        super(cause);
    }
    public WalletNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }
}
