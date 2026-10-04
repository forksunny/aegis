package com.aegis.quote;

public class UnknownProductException extends RuntimeException{
    public UnknownProductException(String productCode){
        super("Unknown product code: " + productCode);
    }
}
