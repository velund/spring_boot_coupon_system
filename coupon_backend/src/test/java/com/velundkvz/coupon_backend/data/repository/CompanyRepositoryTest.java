package com.velundkvz.coupon_backend.data.repository;

import com.velundkvz.coupon_backend.data.model.Company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;
    private Company company;
    @BeforeEach
    private void init() {
        company = Company.builder()
                .uuid(UUID.randomUUID())
                .build();
    }
    @Test
    public void whenFindEmptyReturnsEmptyList() {
        List<Company> list = companyRepository.findAll();
        assertAll(
                ()->assertNotNull(list),
                ()->assertEquals(0, list.size())
        );
    }
    @Test
    public void whenFewCompaniesInDBFindAllReturnsCorrectList() {
        int quantity = 5;
        List<Company> companies = getRandomCompanies(quantity);
        for (Company c : companies) {
            companyRepository.save(c);
        }
        assertEquals(quantity, companyRepository.findAll().size());
    }
    @Test
    public void whenCompanyExistsFindByEmailReturnsCorrectList() {
        final String testingMail = "testingMail@com";
        int quantity = 2;
        List<Company> companies = getRandomCompanies(quantity);
        for (Company c : companies) {
            c.setEmail(testingMail);
            companyRepository.save(c);
        }
        companyRepository.save(company);
        List<Company> companiesFound = companyRepository.findByEmail(testingMail);
        assertAll(
                ()->assertEquals(quantity, companiesFound.size()),
                ()-> {
                    for (Company cmp: companiesFound) {
                        assertEquals(testingMail, cmp.getEmail());
                    }
                }
        );
    }
    @Test
    public void whenCompanyExistsFindByEmailAndPasswordReturnsCorrectOptional() {
        final String testingMail = "testingMail@com";
        int quantity = 2;
        List<Company> companies = getRandomCompanies(quantity);
        for (Company c : companies) {
            c.setEmail(testingMail);
            companyRepository.save(c);
        }
        company.setEmail(testingMail);
        String testingPassword = "testingPAssword";
        company.setPassword(testingPassword);
        companyRepository.save(company);
        Optional<Company> companyFoundOpt = companyRepository.findByEmailAndPassword(testingMail, testingPassword);
        assertTrue(companyFoundOpt.isPresent());
        assertAll(
                ()->assertEquals(testingMail, companyFoundOpt.get().getEmail()),
                ()->assertEquals(testingPassword, companyFoundOpt.get().getPassword())
        );
    }

    private List<Company> getRandomCompanies(int quantity) {
        List<Company> list = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            list.add(Company.builder()
                    .uuid(UUID.randomUUID())
                    .build());
        }
        return list;
    }

}