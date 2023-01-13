package com.velundkvz.coupon_backend.controller;

import aj.org.objectweb.asm.ConstantDynamic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;
import com.velundkvz.coupon_backend.exceptions.CustomerNotFoundException;

import com.velundkvz.coupon_backend.service.CompanyService;
import com.velundkvz.coupon_backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CompanyService companyService;
    @MockBean
    private CustomerService customerService;
    private final String customerControllerUrl = "coupon_system/customer";
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    public void whenTryGetNotExistingCustomerThenReturnStatusNotFound() {
        String fakeEmail = "fakeEmail";
        String fakePassword = "fakePassword";
        String url = "/" + customerControllerUrl + "/?email=%s&password=%s".formatted(fakeEmail, fakePassword);
        when(customerService.getCustomerBy(any(), any())).thenThrow(CustomerNotFoundException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void whenCreateCustomerThenReturnsStatusCreatedWithCorrectLocationInHeader() {
        String url = "/" + customerControllerUrl + "/create";
        String email = "email";
        String password = "password";
        UUID uuid = UUID.randomUUID();
        CustomerDTO customerDTO= CustomerDTO.builder()
                .uuid(uuid)
                .password(password)
                .email(email)
                .build();
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost");
        URI returnedUri = uriBuilder
                .path(url)
                .path("/")
                .query("email={email}")
                .query("password={password}")
                .buildAndExpand(email, password)
                .toUri();
        String jsonTtype = "application/json;charset=UTF-8";
        when(customerService.insert(any())).thenReturn(Optional.of(customerDTO));
        try {
            String jsonCustomerDto = jsonMapper.writeValueAsString(customerDTO);
            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .content(jsonCustomerDto)
                            .contentType(jsonTtype))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.header()
                            .stringValues("Location", returnedUri.toString())
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}