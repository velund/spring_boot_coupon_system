package com.velundkvz.coupon_backend.error_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponErrorResponse {
    private final Timestamp creationTime;
    private final String msg;
    public static CouponErrorResponse create(String msg) {
        return new CouponErrorResponse(Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis())), msg);
    }
}
