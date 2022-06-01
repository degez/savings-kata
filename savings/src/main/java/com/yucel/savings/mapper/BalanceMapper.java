package com.yucel.savings.mapper;

import com.yucel.savings.domain.Balance;
import com.yucel.savings.dto.BalanceDTO;

public class BalanceMapper {

    public static BalanceDTO mapToBalanceDTO(Balance balance) {

        return BalanceDTO.builder().amount(balance.getAmount()).build();
    }

}
