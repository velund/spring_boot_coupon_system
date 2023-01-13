package com.velundkvz.coupon_backend.data.model.DTO;

import com.velundkvz.coupon_backend.data.model.Company;
import com.velundkvz.coupon_backend.data.model.Coupon;
import com.velundkvz.coupon_backend.data.model.Customer;

import java.util.Objects;
import java.util.stream.Collectors;

public class Mappers {
    public static CustomerDTO map(Customer customer) {
        if (Objects.nonNull(customer)) {
            return CustomerDTO.builder()
                    .uuid(customer.getUuid())
                    .first_name(customer.getFirst_name())
                    .last_name((customer.getLast_name()))
                    .email(customer.getEmail())
                    .password(customer.getPassword())
                    .customerDtoCoupons(customer.getCustomerCoupons()
                            .stream().map(Mappers::map)
                            .collect(Collectors.toList())
                    )
                    .build();
        }
        return null;
    }
    public static Customer map(CustomerDTO customerDTO) {
        if (Objects.nonNull(customerDTO)) {
            return Customer.builder()
                    .uuid(customerDTO.getUuid())
                    .first_name(customerDTO.getFirst_name())
                    .last_name((customerDTO.getLast_name()))
                    .email(customerDTO.getEmail())
                    .password(customerDTO.getPassword())
                    .build();
        }
        return null;
    }
    public static CompanyDTO map(Company company) {
        if (Objects.nonNull(company)) {
            return CompanyDTO.builder()
                    .uuid(company.getUuid())
                    .name(company.getName())
                    .email(company.getEmail())
                    .password(company.getPassword())
                    .coupons(company.getCompanyCoupons()
                            .stream().map(Mappers::map)
                            .collect(Collectors.toList()))
                    .build();
        }
        return null;
    }
    public static Company map(CompanyDTO companyDTO) {
        if (Objects.nonNull(companyDTO)) {
            return Company.builder()
                    .uuid(companyDTO.getUuid())
                    .name(companyDTO.getName())
                    .email(companyDTO.getEmail())
                    .password(companyDTO.getPassword())
                    .build();
        }
        return null;
    }
    public static CouponDTO map(Coupon coupon) {
        if (Objects.nonNull(coupon)) {
           return CouponDTO.builder()
                   .title(coupon.getTitle())
                   .category(coupon.getCategory())
                   .price(coupon.getPrice())
                   .description(coupon.getDescription())
                   .endDate(coupon.getEndDate())
                   .startDate(coupon.getStartDate())
                   .imageUrl(coupon.getImageUrl())
                   .uuid(coupon.getUuid())
                   .build();
        }
        return null;
    }
    public static Coupon map(CouponDTO couponDTO) {
        if (Objects.nonNull(couponDTO)) {
            return Coupon.builder()
                    .title(couponDTO.getTitle())
                    .category(couponDTO.getCategory())
                    .price(couponDTO.getPrice())
                    .description(couponDTO.getDescription())
                    .endDate(couponDTO.getEndDate())
                    .startDate(couponDTO.getStartDate())
                    .imageUrl(couponDTO.getImageUrl())
                    .uuid(couponDTO.getUuid())
                    .build();
        }
        return null;
    }
}
