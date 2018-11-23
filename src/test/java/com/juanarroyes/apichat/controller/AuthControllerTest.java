package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.helpers.DataHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {AuthController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractControllerTest {

    @Test
    public void testRegisterUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \""+ DataHelper.getStaticUserEmail() +"\", \"password\":\""+ DataHelper.getStaticPassword() +"\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testRegisterUserAreRegistered() throws Exception {
        Mockito.when(userService.existsByEmail(anyString())).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"" + DataHelper.getStaticUserEmail() + "\", \"password\":\"" + DataHelper.getStaticPassword() + "\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isConflict());
    }

    @Test
    public void testBadParamsRegisterUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"" + DataHelper.getStaticUserEmail() + "\", \"password2\":\""+ DataHelper.getStaticPassword() + "\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"" + DataHelper.getStaticUserEmail() + "\", \"password\":\"" + DataHelper.getStaticPassword() + "\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testRefreshToken() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"" + DataHelper.getStaticUserEmail() + "\", \"password\":\"" + DataHelper.getStaticPassword() + "\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
