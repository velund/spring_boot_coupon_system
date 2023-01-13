package com.velundkvz.coupon_backend.data.repository;

import com.velundkvz.coupon_backend.data.model.Company;
import com.velundkvz.coupon_backend.data.model.Coupon;
import com.velundkvz.coupon_backend.data.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;
    private Company company;
    private Coupon coupon;

    @BeforeEach
    public void init() {
        customer = Customer.builder()
                .uuid(UUID.randomUUID())
                .build();
        company = Company.builder()
                .uuid(UUID.randomUUID())
                .build();
        coupon = getRandomUuidCoupon(customer, company);
        customerRepository.save(customer);
        companyRepository.save(company);
    }

    @Test
    public void whenFindAllFromEmptyDbThenReturnsEmptyList () {
        List<Coupon> list = couponRepository.findAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    @Test
    public void whenFindAllFromNotDbThenReturnsNotEmptyList () {

        couponRepository.save(coupon);
        List<Coupon> list = couponRepository.findAll();
        assertAll(
                ()-> assertNotNull(list),
                ()-> assertEquals(1, list.size())
        );
    }
    @Test
    public void whenFindByUuidThenCorrectOptionalCouponReturned() {
        UUID correctUuid = coupon.getUuid();
        couponRepository.save(coupon);
        Optional<Coupon> optCoupon = couponRepository.findByUuid(correctUuid);
        assertAll(
                () -> assertFalse(optCoupon.isEmpty()),
                () -> assertEquals(correctUuid, optCoupon.get().getUuid())
        );
    }
    @Test
    public void whenFindByNotExistingUuidThenEmptyOptionalCouponReturned() {
        couponRepository.save(coupon);
        Optional<Coupon> optCoupon = couponRepository.findByUuid(UUID.randomUUID());
        assertTrue(optCoupon.isEmpty());
    }
    @Test
    public void whenCustomerHasNoCouponsThenFindByCustomerReturnsEmptyList() {
        Customer cust = Customer.builder().build();
        List<Coupon> list = couponRepository.findByCustomer(customer);
        assertAll(
                ()->assertNotNull(list),
                ()->assertEquals(0, list.size())
        );
    }
    @Test
    public void when1CouponBelongsTo1CustomerThenFindByCustomerReturnsListOfOneCoupon() {
        couponRepository.save(coupon);
        List<Coupon> list = couponRepository.findByCustomer(customer);
        assertAll(
                ()->assertNotNull(list),
                ()->assertEquals(1, list.size()),
                ()->assertEquals(customer.getId(), list.get(0).getCustomer().getId())
        );
    }
    @Test
    public void when1CustomerHas2CouponsThenFindByCustomerReturnsListOf2Coupon() {
        Customer cust = Customer.builder()
                .uuid(UUID.randomUUID())
                .build();
        customerRepository.save(cust);
        List<Coupon> coupons = getListOfRadomUuidCoupons(3, cust);
        for (Coupon c : coupons) {
            couponRepository.save(c);
        }
        List<Coupon> list = couponRepository.findByCustomer(cust);
        assertAll(
                ()->assertNotNull(list),
                ()->assertEquals(3, list.size())
        );
    }
    @Test
    public void whenNoBeforeFindAllBeforeReturnsEmptyList() {
        couponRepository.save(coupon);
        String date = (new Date(0).toString());
        assertEquals(0, couponRepository.findAllBefore(Date.valueOf( date)).size());
    }
    @Test
    public void when1CouponAmong2BeforeFindAllBeforeReturnsListOf1() {
        String zeroDate = (new Date(0).toString());
        Coupon coup = getRandomUuidCoupon(customer, company);
        coupon.setEndDate(Date.valueOf(zeroDate));
        coup.setEndDate(Date.valueOf("2022-12-31"));
        couponRepository.save(coupon);
        couponRepository.save(coup);
        assertEquals(1, couponRepository.findAllBefore(Date.valueOf( "1980-01-01")).size());
    }
    @Test
    public void whenDeleteByExistingUuidThenCorrectCoupondeleted() {
        Coupon couponToDelete = getRandomUuidCoupon(customer, company);
        UUID uuid = UUID.randomUUID();
        couponToDelete.setUuid(uuid);
        couponRepository.save(couponToDelete);
        couponRepository.save(coupon);
        String date = (new Date(0).toString());

        assertTrue(couponRepository.findByUuid(uuid).isPresent());

        couponRepository.deleteByUuid(uuid);
        assertAll(
                ()->assertEquals(1, couponRepository.findAll().size()),
                ()->assertTrue(couponRepository.findByUuid(uuid).isEmpty())
        );

    }

/*
* Helper functions
 */
    private List<Coupon> getListOfRadomUuidCoupons(int listSize, Customer customer) {
        List<Coupon> list = new ArrayList();
        for (int j = 0; j < listSize; j++) {
            list.add(getRandomUuidCoupon(customer, company));
        }
        return list;
    }

    private Coupon getRandomUuidCoupon(Customer cust, Company comp) {
        return Coupon.builder()
                .uuid(UUID.randomUUID())
                .customer(cust)
                .company(comp)
                .build();
    }
}