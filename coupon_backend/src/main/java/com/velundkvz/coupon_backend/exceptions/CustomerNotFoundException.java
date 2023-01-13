package com.velundkvz.coupon_backend.exceptions;

public class CustomerNotFoundException extends RuntimeException{
    public static final String EntityNotFoundWithMailAndPasswordExcFormat = "%s with email %s and password %s, not found";
    public static final String customerNotFoundExc = "customer not found";
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
