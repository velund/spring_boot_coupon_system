package com.velundkvz.coupon_backend.data.model.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.sql.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CouponDTO {
    private UUID uuid;
    private String title;
    private int category;
    private long price;
    private Date startDate;
    private Date endDate;
    private String description;
    private URL imageUrl;
}
