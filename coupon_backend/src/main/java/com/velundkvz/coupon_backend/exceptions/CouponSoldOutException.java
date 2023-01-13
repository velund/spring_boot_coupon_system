package com.velundkvz.coupon_backend.exceptions;

public class CouponSoldOutException extends RuntimeException {
    public CouponSoldOutException(String msg) {
        super(msg);
    }
}
