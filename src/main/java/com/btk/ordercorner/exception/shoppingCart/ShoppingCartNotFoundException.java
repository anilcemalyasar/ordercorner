package com.btk.ordercorner.exception.shoppingCart;

public class ShoppingCartNotFoundException extends RuntimeException {

    public ShoppingCartNotFoundException() {
        super();
    }

    public ShoppingCartNotFoundException(String message) {
        super(message);
    }

    public ShoppingCartNotFoundException(Throwable cause) {
        super(cause);
    }

    public ShoppingCartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
