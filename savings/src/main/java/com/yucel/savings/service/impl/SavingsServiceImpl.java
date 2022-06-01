package com.yucel.savings.service.impl;

import com.yucel.savings.domain.Balance;
import com.yucel.savings.dto.BalanceDTO;
import com.yucel.savings.exception.AccountNotFoundException;
import com.yucel.savings.exception.BalanceCannotBeNegativeException;
import com.yucel.savings.mapper.BalanceMapper;
import com.yucel.savings.repository.BalanceRepository;
import com.yucel.savings.service.SavingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    private static final String BALANCE_CANNOT_BE_NEGATIVE = "Balance cannot be negative";
    private static final String AMOUNT_ERROR_MESSAGE = "amount cannot be null";

    @Value("${ACCOUNT:a}")
    private String account;

    private final BalanceRepository balanceRepository;


    /**
     * our application currently supports only one account, so we init an
     * empty balance for the configured account(a or b according to the env)
     */
    @PostConstruct
    private void initAccount() {
        Optional<Balance> balanceOptional = balanceRepository.findByAccount(account);
        log.info("balance will be created if not exists, current balance: {}", balanceOptional);
        Balance balance = balanceOptional.orElseGet(() -> balanceRepository.save(Balance.builder()
                .account(account)
                .amount(BigDecimal.ZERO)
                .build())
        );
        log.info("balance after initialization: {}", balance);
    }

    /**
     * Returns balance info. Normally we have one balance account,
     * but in case data manipulation on DB there is a chance we get no records
     *
     * @return retrieved balance from DB
     */
    @Override
    public BalanceDTO getBalance() {

        Optional<Balance> balanceOptional = balanceRepository.findByAccount(account);
        log.info("balance information: {}", balanceOptional);

        return balanceOptional.map(BalanceMapper::mapToBalanceDTO).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
    }

    /**
     * Returns updated balance info. Normally we have one balance account,
     * but in case data manipulation on DB there is a chance we get no records
     *
     * @return updated balance on DB
     */
    @Override
    public BalanceDTO updateBalance(BalanceDTO balanceDTO) {
        log.info("requested change for balance: {}", balanceDTO);
        validate(balanceDTO);
        Optional<Balance> balanceOptional = balanceRepository.findByAccount(account);

        log.info("current balance: {}", balanceOptional);

        return balanceOptional.map(balance -> performBalanceOperationAndGetUpdatedBalance(balanceDTO, balance)).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));

    }

    private void validate(BalanceDTO balanceDTO) {
        if(balanceDTO.getAmount() == null) {
            throw new IllegalArgumentException(AMOUNT_ERROR_MESSAGE);
        }
    }

    private BalanceDTO performBalanceOperationAndGetUpdatedBalance(BalanceDTO balanceDTO, Balance balance) {

        BigDecimal amountSum = balance.getAmount().add(balanceDTO.getAmount());

        if(amountSum.compareTo(BigDecimal.ZERO) == -1) {
            log.error("update attempt results in negative balance, current balance: {} - after decrease balance: {}", balance.getAmount(), balanceDTO.getAmount());
            throw new BalanceCannotBeNegativeException(BALANCE_CANNOT_BE_NEGATIVE);
        }
        balance.setAmount(amountSum);
        Balance updatedBalance = balanceRepository.save(balance);
        log.info("updated balance: {}", updatedBalance);
        return BalanceMapper.mapToBalanceDTO(updatedBalance);
    }
}
