package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.data.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    @MockBean
    private CouponRepository couponRepository;
    @Captor
    private ArgumentCaptor<Date> dateCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    @Test
    void whenGetAllCouponsFromEmptyThenReturnsEmtyList() {
        when(couponRepository.findAll()).thenReturn(new ArrayList<>());
        List<CouponDTO> allCoupons = couponService.getAllCoupons();
        verify(couponRepository).findAll();
        assertNotNull(allCoupons);
        assertEquals(0, allCoupons.size());
    }

    @Test
    void getAllExpiredCoupons() {
        couponService.getExpiredCoupons();
        verify(couponRepository).findAllBefore(dateCaptor.capture());
        Date currentDate = new Date(System.currentTimeMillis());
        assertEquals(currentDate.toString(), dateCaptor.getValue().toString());
    }

    @Test
    void remove() {
        UUID uuid = UUID.randomUUID();
        CouponDTO couponDTO = CouponDTO.builder()
                .uuid(uuid)
                .build();
        couponService.remove(couponDTO);
        verify(couponRepository).deleteByUuid(uuidCaptor.capture());
        assertEquals(uuid, uuidCaptor.getValue());
    }
}