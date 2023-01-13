package com.velundkvz.coupon_backend.service;

import com.velundkvz.coupon_backend.data.model.Company;
import com.velundkvz.coupon_backend.data.repository.CompanyRepository;
import com.velundkvz.coupon_backend.exceptions.CompanyNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.lang.model.util.Types;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CompanyServiceTest {
    @Autowired
    private CompanyService companyService;
    @MockBean
    private CompanyRepository companyRepository;
    @Captor
    private ArgumentCaptor<String> stringCaptor1;
    @Captor
    private ArgumentCaptor<String> stringCaptor2;


    @Test
    public void whenFetchingByEmailAndPasswordThenCorrectParamsPassedToCompanyRepository() {
        String email = "email";
        String password = "password";
        when(companyRepository.findByEmailAndPassword(email, password))
                .thenReturn(Optional.of(Company.builder().build()));
        companyService.getCompanyBy(email, password);
        verify(companyRepository).findByEmailAndPassword(stringCaptor1.capture(), stringCaptor2.capture());
        assertAll(
                ()->assertEquals(email,stringCaptor1.getValue()),
                ()->assertEquals(password, stringCaptor2.getValue())
        );
    }
    @Test
    public void whenFetchingNotExistingCompanyThenThrowsCompanyNotFoundException() {
        assertThrows(CompanyNotFoundException.class, ()->companyService.getCompanyBy("", ""));

    }
}
/*
    Optional<CompanyDTO> getCompanyBy(String email, String password);
    Optional<CompanyDTO> insert(CompanyDTO companyDTO);
    void delete(CompanyDTO companyDTO);
    void updateEmail(CompanyDTO companyDTO, String email);
    void updatePassword(CompanyDTO companyDTO, String password);
    Optional<CouponDTO> createCoupon(UUID companyUuid, CouponDTO couponDTO);
    Optional<CouponDTO> addCoupon(UUID companyUuid, UUID couponUuid, int amount)
* */