package com.velundkvz.coupon_backend.controller;

import com.velundkvz.coupon_backend.data.model.DTO.CustomerDTO;
import com.velundkvz.coupon_backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon_system/customer")
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping("/")
    public ResponseEntity<CustomerDTO> getCustomer(@RequestParam String email, @RequestParam String password )
    {
        Optional<CustomerDTO> customerDTOOpt = customerService.getCustomerBy(email, password);
        if (customerDTOOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerDTOOpt.get());
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Optional<CustomerDTO> customerDTOOpt = customerService.insert(customerDTO);
        if (customerDTOOpt.isEmpty()) {
            return ResponseEntity.status(555).body(CustomerDTO.builder().build());
        }
        CustomerDTO gottenCustomerDTO = customerDTOOpt.get();
        String gottenEmail = gottenCustomerDTO.getEmail();
        String gottenPassword = gottenCustomerDTO.getPassword();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/")
                .query("email={email}")
                .query("password={password}")
                .buildAndExpand(gottenEmail, gottenPassword)
                .toUri();
        return ResponseEntity.created(uri).body(gottenCustomerDTO);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> removeCustomer(@PathVariable UUID uuid) {
        customerService.removeByUuid(uuid);
        return ResponseEntity.ok("deleted");
    }

    @PatchMapping("/{uuid}/email_update")
    public ResponseEntity<String> updateCustomersEmail(@PathVariable UUID uuid, @RequestParam String new_email) {
        customerService.updateEmail(uuid, new_email);
        return ResponseEntity.status(201).body("e-mail updated successfully");
    }
    @PatchMapping("/{uuid}/password_update")
    public ResponseEntity<String> updateCustomersPassword(@PathVariable UUID uuid, @RequestParam String new_password) {
        customerService.updatePassword(uuid, new_password);
        return ResponseEntity.status(201).body("password updated successfully");
    }

    @GetMapping("/{customerUuid}/purchase/{couponUuid}")
    public ResponseEntity<String> purchaseCoupon(@PathVariable UUID customerUuid, @PathVariable UUID couponUuid)
    {
        customerService.purchaseCoupon(customerUuid, couponUuid);
        return ResponseEntity.ok( "coupon purchased" );
    }

}
