package com.programmingtechie.orderservice.exception.result;

public class ProductNotInStockException extends RuntimeException {
    private final int result =4000;

    public ProductNotInStockException(String message) {
        super(message);

    }

    public int getResultCode() {
        return result;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}