package com.velundkvz.coupon_backend.data.repository;

import com.velundkvz.coupon_backend.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer save(Customer customer);
    void deleteByUuid(UUID uuid);

    List<Customer> findAll();
    Optional<Customer> findByEmailAndPassword(String email, String password);
    Optional<Customer> findByUuid(UUID uuid);
    List<Customer> findByEmail(String email);

    long count();
}
