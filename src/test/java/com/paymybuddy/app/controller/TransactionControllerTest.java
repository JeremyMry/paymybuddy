package com.paymybuddy.app.controller;

import com.paymybuddy.app.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void getTransaction() throws Exception {
        this.mockMvc.perform(get("/transactions/{transactionId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransactionMade() throws Exception {
        this.mockMvc.perform(get("/transactions/made/{userId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransactionReceived() throws Exception {
        this.mockMvc.perform(get("/transactions/received/{userId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void addTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON).content("{\"reference\": \"john\",\"amount\": 100,\"debtor\": 1,\"creditor\": 2}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    public void addTransactionIncorrectBody() throws Exception {
        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON).content("{\"reference: \"john\",\"amount\": 100,\"debtor\": 1,\"creditor\": 2}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }
}
