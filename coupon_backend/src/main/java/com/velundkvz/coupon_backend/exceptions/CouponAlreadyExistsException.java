package com.velundkvz.coupon_backend.exceptions;

public class CouponAlreadyExistsException extends RuntimeException {
    public CouponAlreadyExistsException(String msg) {
        super(msg);
    }
}
