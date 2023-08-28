package com.btk.ordercorner.exception.customer;

public class CustomerAlreadyExistsException extends RuntimeException{
    public CustomerAlreadyExistsException(){
    }

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
    public CustomerAlreadyExistsException(Throwable cause) {
        super(cause);
    }
    public CustomerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
