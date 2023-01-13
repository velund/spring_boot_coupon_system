package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.Company;
import com.velundkvz.coupon_backend.data.model.Coupon;
import com.velundkvz.coupon_backend.data.model.DTO.CompanyDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.data.repository.CompanyRepository;
import com.velundkvz.coupon_backend.data.repository.CouponRepository;
import com.velundkvz.coupon_backend.exceptions.CompanyNotFoundException;
import com.velundkvz.coupon_backend.exceptions.CouponAlreadyExistsException;
import com.velundkvz.coupon_backend.exceptions.CouponNotFoundException;
import com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.velundkvz.coupon_backend.data.model.DTO.Mappers.map;
import static com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException.customerNotFoundExc;
import static com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException.EntityNotFoundWithMailAndPasswordExcFormat;
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CouponRepository couponRepository;
    @Override
    public Optional<CompanyDTO> getCompanyBy(String email, String password) {
        Optional<Company> optComp = companyRepository.findByEmailAndPassword(email, password);
        if (optComp.isEmpty()) {
            throw new CompanyNotFoundException(String.format(EntityNotFoundWithMailAndPasswordExcFormat,"company", email, password));
        }
        return Optional.of(map(optComp.get()));
    }

    @Override
    public Optional<CompanyDTO> insert(CompanyDTO companyDTO) {
        if (Objects.isNull(companyDTO)) {
            throw new RuntimeException("customerDto is null, not possible to insert");
        }
        CompanyDTO compDTO = map(companyRepository.save(map(companyDTO)));
        if (compDTO == null) {
            return Optional.empty();
        }
        return Optional.of(compDTO);
    }

    @Override
    public void delete(CompanyDTO companyDTO) {
        if (Objects.isNull(companyDTO)) {
            throw new NullPointerException("custumerDto is null, not possible to delete");
        }
        companyRepository.delete(map(companyDTO));
    }

    @Override
    public void updateEmail(CompanyDTO companyDTO, String newEmail) {
        if (Objects.isNull(companyDTO)) {
            throw new NullPointerException("custumerDto is null, not possible to update email");
        }
        String customerEmail = map(companyDTO).getEmail();
        String customerPassword = map(companyDTO).getPassword();
        Optional<Company> optComp = companyRepository.findByEmailAndPassword(customerEmail, customerPassword);
        if (optComp.isEmpty()) {
            throw new CustomerNotFoundException(customerNotFoundExc);
        }
        Company updatedCompany = optComp.get();
        updatedCompany.setEmail(newEmail);
        companyRepository.save(updatedCompany);
    }

    @Override
    public void updatePassword(CompanyDTO companyDTO, String newPassword) {
        if (Objects.isNull(companyDTO)) {
            throw new NullPointerException("custumerDto is null, not possible to update password");
        }
        String customerEmail = map(companyDTO).getEmail();
        String customerPassword = map(companyDTO).getPassword();
        Optional<Company> optCust = companyRepository.findByEmailAndPassword(customerEmail, customerPassword);
        if (optCust.isEmpty()) {
            throw new CustomerNotFoundException(customerNotFoundExc);
        }
        Company updatedCompany = optCust.get();
        updatedCompany.setEmail(newPassword);
        companyRepository.save(updatedCompany);
    }

    @Override
    public Optional<CouponDTO> createCoupon(UUID companyUuid, CouponDTO couponDTO) {
        if (Objects.isNull(couponDTO)) {
            throw new NullPointerException("couponDTO or is null");
        }

        Optional<Company> companyOpt = companyRepository.findByUuid(companyUuid);
        Optional<Coupon> couponOpt = couponRepository.findByUuid(couponDTO.getUuid());
        if (companyOpt.isEmpty()) {
            throw new CustomerNotFoundException( String.format("company number %s not found", companyUuid) );
        }
        if (couponOpt.isPresent()) {
            throw new CouponAlreadyExistsException(String.format("coupon number %s, already exists", couponDTO.getUuid()));
        }

        Coupon savedCoupon = couponRepository.save(map(couponDTO));
        Company company = companyOpt.get();
        company.getCompanyCoupons().add(savedCoupon);
        companyRepository.save(company);
        return Optional.of(map(savedCoupon));
    }
    @Override
    public Optional<CouponDTO> addCoupon(UUID companyUuid, UUID couponUuid, int amount) {
        Optional<Company> companyOpt = companyRepository.findByUuid(companyUuid);
        Optional<Coupon> couponOpt = couponRepository.findByUuid(couponUuid);
        if (companyOpt.isEmpty()) {
            throw new CustomerNotFoundException( String.format("company number %s not found", companyUuid) );
        }
        if (couponOpt.isEmpty()) {
            throw new CouponNotFoundException(String.format("coupon number %s, not found", couponUuid));
        }
        Coupon coupon = couponOpt.get();
        coupon.setAmount(coupon.getAmount() + amount);
        Coupon savedCoupon = couponRepository.save(coupon);
        return Optional.of(map(savedCoupon));
    }
}
