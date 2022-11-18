package com.jpabook.jpashop.exception;

public class NotEnoughItemStockException extends RuntimeException {
    public NotEnoughItemStockException() {
        super();
    }

    public NotEnoughItemStockException(String message) {
        super(message);
    }

    public NotEnoughItemStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughItemStockException(Throwable cause) {
        super(cause);
    }
}
