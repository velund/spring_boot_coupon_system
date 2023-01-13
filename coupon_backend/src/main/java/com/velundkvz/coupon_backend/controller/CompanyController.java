package com.velundkvz.coupon_backend.controller;

import com.velundkvz.coupon_backend.data.model.DTO.CompanyDTO;
import com.velundkvz.coupon_backend.data.model.DTO.CouponDTO;
import com.velundkvz.coupon_backend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon_system/company")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create_company")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        Optional<CompanyDTO> companyDtoOpt = companyService.insert(companyDTO);
        if (companyDtoOpt.isEmpty()) {
            return ResponseEntity.status(777).build();
        }
        CompanyDTO created = companyDtoOpt.get();
        String email = created.getEmail();
        String password = created.getPassword();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/")
                .query("email={email}")
                .query("password={password}")
                .buildAndExpand(email, password)
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/")
    public ResponseEntity<CompanyDTO> getCompany(@RequestParam String email, @RequestParam String password) {
        Optional<CompanyDTO> companyDtoOpt = companyService.getCompanyBy(email, password);
        if (companyDtoOpt.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok( companyDtoOpt.get() );
    }

    @PostMapping("/{uuid}/create_new_coupon")
    public ResponseEntity<CouponDTO> createCoupon(@PathVariable UUID uuid, @RequestBody CouponDTO couponDTO) {
        Optional<CouponDTO> createdCouponDtoOpt = companyService.createCoupon(uuid, couponDTO);
        if (createdCouponDtoOpt.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        UUID uuidOfCreatedCoupon = createdCouponDtoOpt.get().getUuid();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/")
                .pathSegment("coupon", "{uuid}")
                .buildAndExpand( uuidOfCreatedCoupon)
                .toUri();
        return ResponseEntity.created(uri).body(createdCouponDtoOpt.get());
    }
    @PatchMapping("/{uuid}/add_coupon")
    public ResponseEntity<CouponDTO> addCoupon(@PathVariable UUID uuid, @RequestParam UUID couponUuid, @RequestParam int amount) {
        Optional<CouponDTO> couponDtoOpt = companyService.addCoupon(uuid, couponUuid, amount);
        if (couponDtoOpt.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(couponDtoOpt.get());
    }
}
