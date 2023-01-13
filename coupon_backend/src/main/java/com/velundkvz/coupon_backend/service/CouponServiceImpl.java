package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;
import com.velundkvz.coupon_backend.data.model.DTO.Mappers;
import com.velundkvz.coupon_backend.data.repository.CouponRepository;
import com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    @Override
    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll()
                .stream().map(Mappers::map)
                .collect(Collectors.toList());
    }
    @Override
    public List<CouponDTO> getExpiredCoupons()
    {
        return couponRepository.findAllBefore(new Date(System.currentTimeMillis()))
                .stream().map(Mappers::map)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(CouponDTO couponDTO) {
        if (couponDTO == null) {
            throw new NullPointerException("customerDTO is NULL");
        }
        couponRepository.deleteByUuid(couponDTO.getUuid());
    }
}
