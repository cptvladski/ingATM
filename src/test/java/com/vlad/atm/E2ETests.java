package com.vlad.atm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class E2ETests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void deposit_will_not_accept_invalid_bills() throws Exception{
//        when(accountRepository.findByAccountNumber(1234)).thenReturn(new Account(1234,"1111",100));
//        when(billProcessingTool.checkBills(any())).thenReturn(Optional.of(40));
        mockMvc.perform(put("/deposit")
                .contentType("application/json")
                .content("{" +
                        "\"accountNumber\":\"1234\"," +
                        "\"pin\":\"0000\"," +
                        "\"bills\":[1,1,1,7]" +
                        "}"))
                .andExpect(status().isUnprocessableEntity());
    }
}
