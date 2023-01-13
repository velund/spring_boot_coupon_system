package com.velundkvz.coupon_backend.controller;

import com.velundkvz.coupon_backend.error_response.CompanyErrorResponse;
import com.velundkvz.coupon_backend.error_response.CouponErrorResponse;
import com.velundkvz.coupon_backend.error_response.CustomerErrorResponse;
import com.velundkvz.coupon_backend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponSystemAdvise {

    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CompanyErrorResponse handleCompanyNotFound(CompanyNotFoundException e) {
        return CompanyErrorResponse.create("company not found: " + e.getCause());
    }

    @ExceptionHandler(CouponAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CouponErrorResponse handleCouponAlreadyExists(CouponAlreadyExistsException e) {
        return CouponErrorResponse.create("coupon already exists: " + e.getCause());
    }
    @ExceptionHandler(CouponNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CouponErrorResponse handleCouponNotFound(CouponNotFoundException e) {
        return CouponErrorResponse.create("coupon not found: " + e.getCause());
    }
    @ExceptionHandler(CouponSoldOutException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CouponErrorResponse handleCouponSoldOut(CouponSoldOutException e) {
        return CouponErrorResponse.create("coupon sold out: " + e.getCause());
    }
    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomerErrorResponse handleCustomerNotFound(CustomerNotFoundException e) {
        return CustomerErrorResponse.create("customer not found: " + e.getCause());
    }

}
