package com.yucel.savings.controller;

import com.yucel.savings.dto.BalanceDTO;
import com.yucel.savings.service.SavingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SavingsController {
    private final SavingsService savingsService;


    @GetMapping(value = "/balance")
    public BalanceDTO getBalance() {
        log.debug("incoming get balance request");
        return savingsService.getBalance();
    }

    @PostMapping(value = "/balance")
    public BalanceDTO updateBalance(@RequestBody BalanceDTO balanceDTO) {
        log.debug("incoming update balance request: {}", balanceDTO);
        return savingsService.updateBalance(balanceDTO);
    }
}
