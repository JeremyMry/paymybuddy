package com.paymybuddy.app.controller;

import com.paymybuddy.app.service.UserService;
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

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void addUserTest() throws Exception {
        this.mockMvc.perform(post("/profile")
                .contentType(MediaType.APPLICATION_JSON).content("{\"firstName\": \"john\",\"lastName\": \"doe\",\"email\": \"xx\",\"password\": \"eee\",\"wallet\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    public void addUserIncorrectBody() throws Exception {
        this.mockMvc.perform(post("/profile")
                .contentType(MediaType.APPLICATION_JSON).content("{firstName\": \"john\",\"lastName\": \"doe\",\"email\": \"xx\",\"password\": \"eee\",\"wallet\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateUserTest() throws Exception {
        this.mockMvc.perform(put("/profile/put/{userId}", 1)
                    .contentType(MediaType.APPLICATION_JSON).content("{\"firstName\": \"john\",\"lastName\": \"doe\",\"email\": \"xx\",\"password\": \"eee\",\"wallet\": 1}"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
    }

    @Test
    public void updateUserTestIncorrectBody() throws Exception {
        this.mockMvc.perform(put("/profile/put/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON).content("{\"firstName\": john\",\"lastName\": \"doe\",\"email\": \"xx\",\"password\": \"eee\",\"wallet\": 1}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getUserTest() throws Exception {
        this.mockMvc.perform(get("/profile/{userId}", 1))
                    .andExpect(status().isOk());
    }
}
