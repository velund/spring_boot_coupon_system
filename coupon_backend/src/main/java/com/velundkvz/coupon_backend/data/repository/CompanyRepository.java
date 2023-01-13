package com.velundkvz.coupon_backend.data.repository;

import com.velundkvz.coupon_backend.data.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company save(Company company);
    void delete(Company company);
    List<Company> findAll();
    Optional<Company> findByEmailAndPassword(String email, String password);
    Optional<Company> findByUuid(UUID companyUuid);
    List<Company> findByEmail(String email);
    long count();
    boolean existsById(Long id);
}
