package com.yucel.savings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yucel.savings.dto.BalanceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static com.yucel.savings.util.TestDataProvider.AMOUNT_ERROR_MESSAGE;
import static com.yucel.savings.util.TestDataProvider.BALANCE_CANNOT_BE_NEGATIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SavingsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void shouldGetBalanceAsZero() throws Exception {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/balance"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        BalanceDTO balanceDTO = objectMapper.readValue(contentAsString, BalanceDTO.class);

        assertEquals(BigDecimal.ZERO, balanceDTO.getAmount());
    }


    @Test
    public void shouldUpdateBalance() throws Exception {

        BalanceDTO balanceRequest = BalanceDTO.builder()
                .amount(BigDecimal.TEN)
                .build();
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/balance")
                        .content(objectMapper.writeValueAsString(balanceRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        BalanceDTO response = objectMapper.readValue(contentAsString, BalanceDTO.class);

        assertEquals(BigDecimal.TEN, response.getAmount());
    }

    @Test
    public void shouldGetErrorForNegativeBalance() throws Exception {

        BalanceDTO balanceRequest = BalanceDTO.builder()
                .amount(BigDecimal.TEN.negate())
                .build();
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/balance")
                        .content(objectMapper.writeValueAsString(balanceRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString();


        assertEquals(BALANCE_CANNOT_BE_NEGATIVE, contentAsString);
    }

    @Test
    public void shouldGetErrorForNullAmount() throws Exception {

        BalanceDTO balanceRequest = BalanceDTO.builder()
                .build();
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/balance")
                        .content(objectMapper.writeValueAsString(balanceRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();


        assertEquals(AMOUNT_ERROR_MESSAGE, contentAsString);
    }
}