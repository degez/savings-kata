package com.yucel.savings.util;
import com.yucel.savings.domain.Balance;
import com.yucel.savings.dto.BalanceDTO;

import java.math.BigDecimal;
import java.util.Optional;

public class TestDataProvider {


    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String BALANCE_CANNOT_BE_NEGATIVE = "Balance cannot be negative";
    public static final String AMOUNT_ERROR_MESSAGE = "amount cannot be null";


    public static Optional<Balance> getInitialBalance(){
        Balance balance=new Balance();
        balance.setId(1L);
        balance.setAmount(BigDecimal.ZERO);
        return Optional.of(balance);
    }

    public static BalanceDTO getUpdatedBalanceDto(){
        BalanceDTO balanceDTO=new BalanceDTO();
        BigDecimal amount= new BigDecimal(7);
        balanceDTO.setAmount(amount);
        return balanceDTO;
    }

    public static Balance getUpdatedBalance(BalanceDTO request, Balance oldBalance){
        Balance balance=new Balance();
        balance.setAmount(oldBalance.getAmount().add(request.getAmount()));
        return balance;
    }

}
