package com.aisencode.fraud.service;

import com.aisencode.fraud.FraudCheckHistory;
import com.aisencode.fraud.repository.FraudCheckHistoryRepository;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({FraudCheckHistory.class, FraudCheckService.class})
class FraudCheckServiceTest {

    @MockBean
    FraudCheckHistoryRepository fraudCheckHistoryRepository;

    @Autowired
    FraudCheckService fraudCheckService;

    @Test
    void isFraudulentCustomer() {
        // GIVEN
        Long customerId = 12345L;
        FraudCheckHistory history = FraudCheckHistory.builder()
                .customerId(customerId)
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(fraudCheckHistoryRepository.save(history))
                .thenReturn(history);

        boolean fraudulentCustomer = fraudCheckService.isFraudulentCustomer(customerId);

        Assertions.assertFalse(fraudulentCustomer);
    }
}