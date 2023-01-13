package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.Coupon;
import com.velundkvz.coupon_backend.data.model.Customer;
import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;
import static com.velundkvz.coupon_backend.data.model.DTO.Mappers.map;

import com.velundkvz.coupon_backend.data.repository.CouponRepository;
import com.velundkvz.coupon_backend.data.repository.CustomerRepository;
import com.velundkvz.coupon_backend.exceptions.CouponNotFoundException;
import com.velundkvz.coupon_backend.exceptions.CouponSoldOutException;
import com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException.customerNotFoundExc;
import static com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException.EntityNotFoundWithMailAndPasswordExcFormat;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    @Override
    public Optional<CustomerDTO> getCustomerBy(String email, String password) {
        Optional<Customer> optCust = customerRepository.findByEmailAndPassword(email, password);
        if (optCust.isEmpty()) {
            throw new CustomerNotFoundException(String.format(EntityNotFoundWithMailAndPasswordExcFormat,"customer", email, password));
        }
        return Optional.of(map(optCust.get()));
    }

    @Override
    public Optional<CustomerDTO> insert(CustomerDTO customerDto) {
       if (Objects.isNull(customerDto)) {
           throw new NullPointerException("customerDto is null, not possible to insert");
       }
        CustomerDTO custDto = map(customerRepository.save(map(customerDto)));
        if (custDto == null) {
            return Optional.empty();
        }
        return Optional.of(custDto);
    }
    @Override
    public void removeByUuid(UUID uuid) {
        customerRepository.deleteByUuid(uuid);
    }
    @Override
    public void updateEmail(UUID uuid, String newEmail) {
        Optional<Customer> optCust = customerRepository.findByUuid(uuid);
        if (optCust.isEmpty()) {
            throw new CustomerNotFoundException(customerNotFoundExc);
        }
        Customer updatedCustomer = optCust.get();
        updatedCustomer.setEmail(newEmail);
        customerRepository.save(updatedCustomer);
    }

    @Override
    public void updatePassword(UUID uuid, String newPassword) {
        Optional<Customer> optCust = customerRepository.findByUuid(uuid);
        if (optCust.isEmpty()) {
            throw new CustomerNotFoundException(customerNotFoundExc);
        }
        Customer updatedCustomer = optCust.get();
        updatedCustomer.setEmail(newPassword);
        customerRepository.save(updatedCustomer);
    }

    @Override
    public void purchaseCoupon(UUID customerUuid, UUID couponUuid) {
        Optional<Customer> customerOpt = customerRepository.findByUuid(customerUuid);
        if (customerOpt.isEmpty()) {
            throw new CustomerNotFoundException("customer not found");
        }
        Optional<Coupon> couponOpt = couponRepository.findByUuid(couponUuid);
        if (couponOpt.isEmpty()) {
            throw new CouponNotFoundException("coupon not found so cannot be purchased");
        }
        Customer customer = customerOpt.get();
        Coupon coupon = couponOpt.get();
        int amount = coupon.getAmount();
        if (amount <= 0) {
            throw new CouponSoldOutException(String.format( "Coupon number %s is sold out", couponUuid));
        }
        coupon.setAmount(amount-1);
        couponRepository.save(coupon);
        customer.getCustomerCoupons().add(coupon);
        customerRepository.save(customer);

    }

}
