package com.aisencode.fraud.controller;

import com.aisencode.fraud.service.FraudCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FraudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FraudCheckService fraudCheckService;

    @Test
    void isFraudster() throws Exception{
        Long customerId = 12345L;

        given(fraudCheckService.isFraudulentCustomer(customerId)).willReturn(false);

        mockMvc.perform(
                get("/api/v1/fraud-check/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFraudster").exists())
                .andDo(print());

        // Verify that the method ran
        verify(fraudCheckService).isFraudulentCustomer(customerId);
    }

}