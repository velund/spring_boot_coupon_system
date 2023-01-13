package com.velundkvz.coupon_backend.data.repository;

import com.velundkvz.coupon_backend.data.model.Company;
import com.velundkvz.coupon_backend.data.model.Customer;
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
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;

    @BeforeEach
    private void init() {
        customer = Customer.builder()
                .uuid(UUID.randomUUID())
                .build();
    }
    @Test
    public void whenFindAllEmptyReturnsEmptyList() {
        List<Customer> list = customerRepository.findAll();
        assertAll(
                ()->assertNotNull(list),
                ()->assertEquals(0, list.size())
        );
    }
    @Test
    public void whenFewCompaniesInDBFindAllReturnsCorrectList() {
        int quantity = 5;
        List<Customer> customers = getRandomCustomers(quantity);
        for (Customer c : customers) {
            customerRepository.save(c);
        }
        assertEquals(quantity, customerRepository.findAll().size());
    }
    @Test
    public void whenCompanyExistsFindByEmailReturnsCorrectList() {
        final String testingMail = "testingMail@com";
        int quantity = 2;
        List<Customer> customers = getRandomCustomers(quantity);
        for (Customer c : customers) {
            c.setEmail(testingMail);
            customerRepository.save(c);
        }
        customerRepository.save(customer);
        List<Customer> companiesFound = customerRepository.findByEmail(testingMail);
        assertAll(
                ()->assertEquals(quantity, companiesFound.size()),
                ()-> {
                    for (Customer cus: companiesFound) {
                        assertEquals(testingMail, cus.getEmail());
                    }
                }
        );
    }
    @Test
    public void whenCompanyExistsFindByEmailAndPasswordReturnsCorrectList() {
        final String testingMail = "testingMail@com";
        int quantity = 2;
        List<Customer> customers = getRandomCustomers(quantity);
        for (Customer c : customers) {
            c.setEmail(testingMail);
            customerRepository.save(c);
        }
        customer.setEmail(testingMail);
        String testingPassword = "testingPAssword";
        customer.setPassword(testingPassword);
        customerRepository.save(customer);
        Optional<Customer> customerFoundOpt = customerRepository.findByEmailAndPassword(testingMail, testingPassword);
        assertTrue(customerFoundOpt.isPresent());
        assertAll(
                ()->assertEquals(testingMail, customerFoundOpt.get().getEmail()),
                ()->assertEquals(testingPassword, customerFoundOpt.get().getPassword())
        );
    }

    @Test
    public void whenCallSaveThenCorrectCustomerReturned(){
        Customer cust = customerRepository.save(customer);
        assertNotNull(cust);
        assertEquals(customer, cust);

    }

    @Test
    public void whenNotExistsFindByUuidReturnsEmptyOptional() {
        assertTrue(customerRepository.findByUuid(UUID.randomUUID()).isEmpty());
    }

    @Test
    public void whenDoesExistsReturnsCorrectCustomer() {
        UUID uuid = UUID.randomUUID();
        String name = "name";
        Customer cust = Customer.builder().uuid(uuid).first_name(name).build();
        customerRepository.save(cust);
        Optional<Customer> custOpt = customerRepository.findByUuid(uuid);
        assertTrue(custOpt.isPresent());
        assertEquals(cust, custOpt.get());
    }

    private List<Customer> getRandomCustomers(int quantity) {
        List<Customer> list = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            list.add(Customer.builder()
                            .uuid(UUID.randomUUID())
                    .build());
        }
        return list;
    }

}