package com.yucel.savings.service;

import com.yucel.savings.exception.AccountNotFoundException;
import com.yucel.savings.repository.BalanceRepository;
import com.yucel.savings.service.impl.SavingsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.yucel.savings.util.TestDataProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavingsServiceTest {

    @InjectMocks
    SavingsServiceImpl savingsService;

    @Mock
    BalanceRepository balanceRepository;

    @Test
    void shouldThrowExceptionWhenAccountIsUnknown(){
        when(balanceRepository.findByAccount(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->savingsService.getBalance())
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageStartingWith(ACCOUNT_NOT_FOUND_MESSAGE);
    }

    @Test
    void shouldUpdateAndReturnBalance(){
        when(balanceRepository.findByAccount(any())).thenReturn(getInitialBalance());
        when(balanceRepository.save(any())).thenReturn(getUpdatedBalance(getUpdatedBalanceDto(), getInitialBalance().get()));
        assertThat( savingsService.updateBalance(getUpdatedBalanceDto()).getAmount()).isEqualTo(getUpdatedBalanceDto().getAmount());
    }

    @Test
    void shouldGetInitialBalance(){
        when(balanceRepository.findByAccount(any()))
                .thenReturn(getInitialBalance());

        assertThatNoException().isThrownBy(()->savingsService.getBalance());
        assertThat(savingsService.getBalance().getAmount()).isEqualTo(BigDecimal.ZERO);
    }

}