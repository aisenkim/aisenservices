package com.aisencode.clients.fraud;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("fraud")
public interface FraudClient {

    // THIS WILL TARGET FRAUD CONTROLLER
    // EXACTLY SAME AS THE METHOD IN FRAUD CONTROLLER WITHOUT METHOD BODY
    // THIS IS WHAT OPEN FEIGN DOES
    // NOW DON'T HAVE TO USE REST CONTROLLER INSIDE CUSTOMER SERVICE
    @GetMapping(path = "api/v1/fraud-check/{customerId}")
    FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId);
}
