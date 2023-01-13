package com.velundkvz.coupon_backend.error_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CompanyErrorResponse {
    private final Timestamp currentTime;
    private final String msg;
    public static CompanyErrorResponse create(String msg) {
        return new CompanyErrorResponse( Timestamp.from( Instant.ofEpochMilli(System.currentTimeMillis()) ), msg );
    }
}
