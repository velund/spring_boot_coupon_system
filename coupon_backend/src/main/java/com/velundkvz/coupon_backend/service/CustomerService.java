package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<CustomerDTO> getCustomerBy(String email, String password);
    Optional<CustomerDTO> insert(CustomerDTO customerDTO);
    void removeByUuid(UUID uuid);
    void updateEmail(UUID uuid, String newEmail);
    void updatePassword(UUID uuid, String newPassword);
    void purchaseCoupon(UUID customerUuid, UUID couponUuid);

}
