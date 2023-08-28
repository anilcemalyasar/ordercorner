package com.btk.ordercorner.exception.category;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(){
    }

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
    public CategoryAlreadyExistsException(Throwable cause) {
        super(cause);
    }
    public CategoryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
