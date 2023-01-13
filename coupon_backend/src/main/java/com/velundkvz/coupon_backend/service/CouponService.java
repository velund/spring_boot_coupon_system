package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;

import java.util.List;

public interface CouponService {
    List<CouponDTO> getAllCoupons();
    List<CouponDTO> getExpiredCoupons();
    void remove(CouponDTO couponDTO);

}
