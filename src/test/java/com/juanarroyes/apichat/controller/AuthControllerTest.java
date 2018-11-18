package com.juanarroyes.apichat.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {AuthController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractControllerTest {

    @Test(expected = JsonMappingException.class)
    @WithMockUser(username = USER_EMAIL, password = USER_PASSWORD)
    public void testRegisterUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \""+USER_EMAIL+"\", \"password\":\""+USER_PASSWORD+"\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

}
