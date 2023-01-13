package com.velundkvz.coupon_backend.exceptions;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String s) {
        super(s);
    }
}
