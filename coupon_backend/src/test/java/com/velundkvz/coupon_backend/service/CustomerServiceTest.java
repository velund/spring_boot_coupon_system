package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.Coupon;
import com.velundkvz.coupon_backend.data.model.Customer;
import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;
import com.velundkvz.coupon_backend.data.repository.CouponRepository;
import com.velundkvz.coupon_backend.data.repository.CustomerRepository;
import com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static com.velundkvz.coupon_backend.data.model.DTO.Mappers.map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private CouponRepository couponRepository;
    @Captor
    private ArgumentCaptor<Customer> customerCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    @Captor
    private ArgumentCaptor<Coupon> couponCaptor;

    @Test
    public void whenCallToInsertThenSaveCalled() {
        CustomerDTO customerDTO = CustomerDTO.builder().build();
        customerService.insert(customerDTO);
        Mockito.verify(customerRepository).save(customerCaptor.capture());
        assertEquals(customerDTO, map(customerCaptor.getValue()));

    }
    @Test
    public void whenFindNotExistingCustomerThenFindByEmailAndPasswordCalled() {
        String email = "email";
        String passWord = "password";
        assertThrows(CustomerNotFoundException.class, ()->customerService.getCustomerBy(email, passWord));
        Mockito.verify(customerRepository).findByEmailAndPassword(email, passWord);
    }
    @Test
    public void whenDeleteThenRepoDeleteCalled() {
        UUID uuid = UUID.randomUUID();
        customerService.removeByUuid(uuid);
        Mockito.verify(customerRepository).deleteByUuid(uuidCaptor.capture());
        assertEquals(uuid, uuidCaptor.getValue());
    }
    @Test
    public void whenUpdateMailCalledThenSaveCalledWithUpdatedCustomer() {
        String oldEmail = "old_email";
        String oldPassword = "old_password";
        String newEmail = "new_email";
        UUID uuid = UUID.randomUUID();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .uuid(uuid)
                .email(oldEmail)
                .password(oldPassword)
                .build();
        Customer updatedCustomer = map(customerDTO);

        when( customerRepository.findByUuid(uuid))
                .thenReturn(Optional.of(updatedCustomer));
        customerService.updateEmail(uuid, newEmail);
        updatedCustomer.setEmail(newEmail);
        Mockito.verify(customerRepository).save(customerCaptor.capture());
        assertEquals(updatedCustomer, customerCaptor.getValue());
    }
    @Test
    public void whenTryToUpdateNotExistingCustomerThenThrowCustomerNotFoundException() {
        assertThrows(CustomerNotFoundException.class, ()->customerService.updateEmail(UUID.randomUUID(), ""));
    }
    @Test
    public void whenUpdatePasswordCalledThenSaveCalledWithUpdatedCustomer() {
        String oldEmail = "old_email";
        String oldPassword = "old_password";
        String newPassword = "new_password";
        UUID uuid = UUID.randomUUID();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .uuid(uuid)
                .email(oldEmail)
                .password(oldPassword)
                .build();
        Customer updatedCustomer = map(customerDTO);

        when( customerRepository.findByUuid(uuid))
                .thenReturn(Optional.of(updatedCustomer));
        customerService.updatePassword(uuid, newPassword);
        updatedCustomer.setPassword(newPassword);
        Mockito.verify(customerRepository).save(customerCaptor.capture());
        assertEquals(updatedCustomer, customerCaptor.getValue());
    }
    @Test
    public void whenCouponPurchasedThenSaveCalledWithCustomerWithUpdatedCouponsList() {
        UUID customerUuid = UUID.randomUUID();
        UUID couponUuid = UUID.randomUUID();
        Optional<Coupon> couponOpt =
                Optional.of(Coupon.builder()
                        .uuid(couponUuid)
                        .amount(1)
                        .build());
        Optional<Customer> customerOpt =
                Optional.of(Customer.builder()
                        .uuid(customerUuid)
                        .build());
        when(customerRepository.findByUuid(customerUuid)).thenReturn(customerOpt);
        when(couponRepository.findByUuid(couponUuid)).thenReturn(couponOpt);

        customerService.purchaseCoupon(customerUuid, couponUuid);
        verify(customerRepository).findByUuid(uuidCaptor.capture());
        assertEquals(customerUuid, uuidCaptor.getValue());
        verify(couponRepository).findByUuid(uuidCaptor.capture());
        assertEquals(couponUuid, uuidCaptor.getValue());
        verify(customerRepository).save(customerCaptor.capture());
        assertTrue( customerCaptor.getValue().getCustomerCoupons().contains(couponOpt.get()));
    }
    @Test
    public void whenCouponPurchasedThenAmountDecreasedByOne() {
        UUID customerUuid = UUID.randomUUID();
        UUID couponUuid = UUID.randomUUID();
        int initailAmount = 2;
        Optional<Coupon> couponOpt =
                Optional.of(Coupon.builder()
                        .uuid(couponUuid)
                        .amount(initailAmount)
                        .build());
        Optional<Customer> customerOpt =
                Optional.of(Customer.builder()
                        .uuid(customerUuid)
                        .build());
        when(customerRepository.findByUuid(customerUuid)).thenReturn(customerOpt);
        when(couponRepository.findByUuid(couponUuid)).thenReturn(couponOpt);

        customerService.purchaseCoupon(customerUuid, couponUuid);
        verify(couponRepository).save(couponCaptor.capture());
        verify(customerRepository).save(customerCaptor.capture());
        assertTrue( customerCaptor.getValue().getCustomerCoupons().contains(couponOpt.get()));
        assertEquals(initailAmount-1, couponCaptor.getValue().getAmount());
    }
}