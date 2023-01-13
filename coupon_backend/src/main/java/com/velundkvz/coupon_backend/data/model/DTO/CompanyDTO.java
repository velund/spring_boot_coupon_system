package com.velundkvz.coupon_backend.data.model.DTO;

import com.velundkvz.coupon_backend.data.model.Coupon;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CompanyDTO {
    private UUID uuid;
    private String name;
    private String email;
    private String password;
    private final List<CouponDTO> coupons;
}
