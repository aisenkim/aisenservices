package com.aisencode.customer.controller;

import com.aisencode.customer.Customer;
import com.aisencode.customer.dto.CustomerRegistrationRequest;
import com.aisencode.customer.dto.CustomerRegistrationResponse;
import com.aisencode.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerRegistrationResponse> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("New customer registration {}", customerRegistrationRequest);
        CustomerRegistrationResponse customer = customerService.registerCustomer(customerRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id") // #id -> parameter name and principal.id can be defined inside the UserPrincipal class constructor
//    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok().body(customerService.getCustomers());
    }

}
