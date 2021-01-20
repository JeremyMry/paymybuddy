package com.paymybuddy.app.controller;

import com.paymybuddy.app.service.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
@ExtendWith(SpringExtension.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    public void getContact() throws Exception {
        this.mockMvc.perform(get("/contact/{contactId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllContact() throws Exception {
        this.mockMvc.perform(get("/contact/all/{userId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void addContact() throws Exception {
        this.mockMvc.perform(post("/contact")
                .contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"xx\",\"firstName\": \"john\",\"user\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    public void addContactIncorrectBody() throws Exception {
        this.mockMvc.perform(post("/contact")
                .contentType(MediaType.APPLICATION_JSON).content("{\"email\": xx\",\"firstName\": \"john\",\"user\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateContact() throws Exception {
        this.mockMvc.perform(put("/contact/put/{contactId}", 1)
                .contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"xx\",\"firstName\": \"john\",\"user\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateContactIncorrectBody() throws Exception {
        this.mockMvc.perform(put("/contact/put/{contactId}", 1)
                .contentType(MediaType.APPLICATION_JSON).content("{\"email\": xx\",\"firstName\": \"john\",\"user\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteContact() throws Exception {
        this.mockMvc.perform(delete("/contact/delete/{contactId}", 1))
                .andExpect(status().isOk());
    }
}
