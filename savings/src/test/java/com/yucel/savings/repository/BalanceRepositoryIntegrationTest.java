package com.yucel.savings.repository;

import com.yucel.savings.domain.Balance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BalanceRepositoryIntegrationTest {

    private static String ACCOUNT = "B";

    @Autowired
    BalanceRepository balanceRepository;

    @Test
    void testSaveBalance() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.TEN);
        balance.setAccount(ACCOUNT);

        Balance updatedBalance = balanceRepository.save(balance);


        Optional<Balance> optionalBalance = balanceRepository.findByAccount(ACCOUNT);


        assertTrue(optionalBalance.isPresent());
        assertEquals(0, optionalBalance.get().getAmount().compareTo(BigDecimal.TEN));

    }
}