package com.velundkvz.coupon_backend;

import com.velundkvz.coupon_backend.data.model.Coupon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class CouponBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponBackendApplication.class, args);
	}


}
