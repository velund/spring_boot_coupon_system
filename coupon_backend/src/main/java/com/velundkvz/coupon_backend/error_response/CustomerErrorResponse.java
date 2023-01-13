package com.velundkvz.coupon_backend.error_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CustomerErrorResponse {
    private final Timestamp creationTime;
    private final String msg;
    public static CustomerErrorResponse create(String msg) {
        return new CustomerErrorResponse(Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis())), msg);
    }
}
