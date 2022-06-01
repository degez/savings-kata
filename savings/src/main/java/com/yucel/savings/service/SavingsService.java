package com.yucel.savings.service;

import com.yucel.savings.dto.BalanceDTO;

public interface SavingsService {
    BalanceDTO getBalance();

    BalanceDTO updateBalance(BalanceDTO balanceDTO);
}
