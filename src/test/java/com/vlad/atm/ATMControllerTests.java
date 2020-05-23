package com.vlad.atm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ATMController.class)
public class ATMControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private BillProcessingTool billProcessingTool;
    @Test
    public void withdraw_requires_all_parameters() throws Exception {
        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"0000",100));
        when(billProcessingTool.split(anyInt())).thenReturn(Optional.of(new ArrayList<>()));
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","0000"))
                .andExpect(status().isUnprocessableEntity());
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("amountWithdrawn","63"))

                .andExpect(status().isUnprocessableEntity());
        mockMvc.perform(get("/withdraw")
                .param("PIN","0000")
                .param("amountWithdrawn","63"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void withdraw_returns_ok_if_all_params_present() throws Exception{
        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"0000",100));
        when(billProcessingTool.split(anyInt())).thenReturn(Optional.of(new ArrayList<>()));

        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","0000")
                .param("amountWithdrawn","63"))
                .andExpect(status().isOk());
    }

    @Test
    public void withdraw_returns_401_if_account_not_found() throws Exception{
        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"0000",100));
        when(billProcessingTool.split(anyInt())).thenReturn(Optional.of(new ArrayList<>()));
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1235")
                .param("PIN","0000")
                .param("amountWithdrawn","63"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void withdraw_returns_401_if_PIN_wrong() throws Exception{
        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"0000",100));
        when(billProcessingTool.split(anyInt())).thenReturn(Optional.of(new ArrayList<>()));
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","0001")
                .param("amountWithdrawn","63"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void withdraw_returns_422_when_amount_negative() throws Exception{
        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"0000",100));
        when(billProcessingTool.split(anyInt())).thenReturn(Optional.of(new ArrayList<>()));
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","0000")
                .param("amountWithdrawn","-63"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void withdraw_returns_422_when_invalid_PIN() throws Exception{
        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"0000",100));
        when(billProcessingTool.split(anyInt())).thenReturn(Optional.of(new ArrayList<>()));
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","00a")
                .param("amountWithdrawn","63"))
                .andExpect(status().isUnprocessableEntity());
    }
}
