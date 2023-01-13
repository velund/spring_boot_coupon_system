package com.velundkvz.coupon_backend.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String msg) {
        super(msg);
    }
}
