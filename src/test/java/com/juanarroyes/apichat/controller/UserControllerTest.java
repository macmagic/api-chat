package com.juanarroyes.apichat.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {UserController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class UserControllerTest extends AbstractControllerTest{

    @Test
    public void testCreateProfile () throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/profile")
                .accept(MediaType.APPLICATION_JSON)
                .content("user_id=22&param1=2")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

}
