package com.velundkvz.coupon_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velundkvz.coupon_backend.data.model.DTO.CompanyDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.exceptions.CompanyNotFoundException;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest
class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CompanyService companyService;
    @MockBean
    private CustomerService customerService;
    private final String companyControllerUrl = "coupon_system/company";
    private final String jsonTtype = "application/json;charset=UTF-8";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost");
    @Test
    void createCompanyReturnsStatusCreatedWithCorrectLocationInHeader() {
        String url = "/"+companyControllerUrl+"/create_company";
        String email = "email";
        String password = "password";
        URI returnedUri = uriBuilder
                .path(url)
                .path("/")
                .query("email={email}")
                .query("password={password}")
                .buildAndExpand(email, password)
                .toUri();

        UUID uuid = UUID.randomUUID();
        CompanyDTO compDto = CompanyDTO.builder()
                .uuid(uuid)
                .email(email)
                .password(password)
                .build();

        when(companyService.insert(any())).thenReturn(Optional.of(compDto));

        try {
            String jsonCompDto = jsonMapper.writeValueAsString(compDto);
            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(jsonTtype)
                            .content(jsonCompDto)
                    )
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.header()
                            .stringValues("Location", returnedUri.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void whenGetExistingCompanyReturnedStatusOK() {
        String url = "/"+companyControllerUrl +"/?email=fff&password=aaa";
        UUID uuid = UUID.randomUUID();
        CompanyDTO compDto = CompanyDTO.builder().uuid(uuid).build();
        when(companyService.getCompanyBy(any(), any())).thenReturn(Optional.of(compDto));
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createCouponReturnsStatusCreatedAndCorrectLocationInHeader() {
        UUID uuid = UUID.randomUUID();
        String url = companyControllerUrl + "/"+uuid+"/create_new_coupon";
        CouponDTO couponDto = CouponDTO.builder().uuid(uuid).build();
        URI returnedUri = uriBuilder
                .pathSegment("coupon", "{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        when( companyService.createCoupon(any(), any()) )
                .thenReturn(Optional.of(couponDto));
        try {
            String jsonCouponDto = jsonMapper.writeValueAsString(couponDto);
            mockMvc.perform(MockMvcRequestBuilders.post("/"+url)
                            .contentType(jsonTtype)
                            .content(jsonCouponDto))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.header()
                            .stringValues("Location", returnedUri.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void whenAddCouponThenReturnsStatusOK() {
        UUID uuid = UUID.randomUUID();
        UUID couponUuid = UUID.randomUUID();
        int amount = 3;
        URI uri = uriBuilder
                .pathSegment(companyControllerUrl, "{uuid}", "add_coupon")
                .query("couponUuid={couponUuid}")
                .query("amount={amount}")
                .buildAndExpand(uuid, couponUuid, amount)
                .toUri();
        Optional<CouponDTO> couponDtoOpt = Optional.of(CouponDTO.builder().build());
        System.out.println(uri);
        when(companyService.addCoupon(any(), any(), anyInt()))
                .thenReturn(couponDtoOpt);
        try {
            mockMvc.perform(MockMvcRequestBuilders.patch(uri))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void whenTryToGetNotExistingCompanyThenStatusNotFound() {
        String fakeEmail = "";
        String fakePAssword = "";
        String url = "/" + companyControllerUrl + "/?email=%s&password=%s".formatted(fakeEmail, fakePAssword);
        when(companyService.getCompanyBy(any(), any())).thenThrow(CompanyNotFoundException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}