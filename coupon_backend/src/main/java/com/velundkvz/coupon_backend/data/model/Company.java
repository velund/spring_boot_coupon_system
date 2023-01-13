package com.velundkvz.coupon_backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, updatable = false, nullable = false, unique = true)
    private UUID uuid;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
    @OneToMany(mappedBy = "company")
    private final List<Coupon> companyCoupons = new ArrayList<>();
}
