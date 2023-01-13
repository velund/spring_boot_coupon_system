package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.DTO.CompanyDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;

import java.util.Optional;
import java.util.UUID;

public interface CompanyService {
    Optional<CompanyDTO> getCompanyBy(String email, String password);
    Optional<CompanyDTO> insert(CompanyDTO companyDTO);
    void delete(CompanyDTO companyDTO);
    void updateEmail(CompanyDTO companyDTO, String email);
    void updatePassword(CompanyDTO companyDTO, String password);
    Optional<CouponDTO> createCoupon(UUID companyUuid, CouponDTO couponDTO);

    Optional<CouponDTO> addCoupon(UUID companyUuid, UUID couponUuid, int amount);
}
