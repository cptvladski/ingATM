package com.vlad.atm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class E2ETests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void deposit_will_not_accept_invalid_bills() throws Exception{
        mockMvc.perform(put("/deposit")
                .contentType("application/json")
                .content("{" +
                        "\"accountNumber\":\"1234\"," +
                        "\"pin\":\"0000\"," +
                        "\"bills\":[1,1,1,7]" +
                        "}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void withdraw_bills_add_up_to_original_amount() throws Exception{

        int amount = 53;
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","0000")
                .param("amountWithdrawn","" + amount))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(3)))
        .andExpect(jsonPath("$[0]",is(50)))
        .andExpect(jsonPath("$[1]",is(2)))
        .andExpect(jsonPath("$[2]",is(1)));
    }

    @Test
    public void consult_after_deposit_yields_expected_amount() throws Exception {
        mockMvc.perform(put("/deposit")
                .contentType("application/json")
                .content("{" +
                        "\"accountNumber\":\"1234\"," +
                        "\"pin\":\"0000\"," +
                        "\"bills\":[1,1,1,1]" +
                        "}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/consult")
                .param("accountNumber","1234")
                .param("PIN","0000"))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$",is(104)));
    }

    @Test
    public void consult_after_withdraw_yields_expected_amount() throws Exception{
        mockMvc.perform(get("/withdraw")
                .param("accountNumber","1234")
                .param("PIN","0000")
                .param("amountWithdrawn","53"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/consult")
                .param("accountNumber","1234")
                .param("PIN","0000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",is(47)));
    }

//    @Test
//    public void
}
