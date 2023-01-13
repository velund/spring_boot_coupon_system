package com.velundkvz.coupon_backend.data.model.DTO;

import com.velundkvz.coupon_backend.data.model.Coupon;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private UUID uuid;
    private String first_name;
    private String last_name;
    private String email;
    private String password;

    private final List<CouponDTO> customerDtoCoupons;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return Objects.equals(first_name, that.first_name) && Objects.equals(last_name, that.last_name) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, last_name, email);
    }
}

