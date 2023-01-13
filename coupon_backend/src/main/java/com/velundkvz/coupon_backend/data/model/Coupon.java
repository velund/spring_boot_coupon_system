package com.velundkvz.coupon_backend.data.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.net.URL;
import java.sql.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "uuid")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = false, updatable = false)
    private long id;
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, updatable = false, nullable = false, unique = true)
    private UUID uuid;
    @Column
    private String title;
    @Column
    private int category;
    @Column
    private long price;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private int amount;
    @Column
    private String description;
    @Column
    private URL imageUrl;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Company company;
}
