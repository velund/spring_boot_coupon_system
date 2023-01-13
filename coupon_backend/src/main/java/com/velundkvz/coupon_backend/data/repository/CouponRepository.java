package com.velundkvz.coupon_backend.data.repository;

import com.velundkvz.coupon_backend.data.model.Coupon;
import com.velundkvz.coupon_backend.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon save(Coupon coupon);
    List<Coupon> findAll();
    Optional<Coupon> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
    List<Coupon> findByCustomer(Customer customer);
    @Query("select c from Coupon c where endDate < :date")
    List<Coupon> findAllBefore(Date date);

}
