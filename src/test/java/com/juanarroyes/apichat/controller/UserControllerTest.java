package com.juanarroyes.apichat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juanarroyes.apichat.exception.UserProfileNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfileKey;
import com.juanarroyes.apichat.request.UserProfileRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.activation.DataHandler;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {UserController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void testCreateProfile () throws Exception {
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setFirstname("Pepe");
        userProfileRequest.setLastname("Navarro");
        userProfileRequest.setNickname("Bacu");
        userProfileRequest.setBirthday("1987-02-01");
        userProfileRequest.setCountry("Spain");
        userProfileRequest.setLocation("Madrid");
        userProfileRequest.setAddress("Fake Street Nº 123 4r-1a Vallecas");
        userProfileRequest.setPhone("55542454");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/profile")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .content(objectMapper.writeValueAsString(userProfileRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testGetProfile () throws Exception {
        Mockito.when(userProfileService.getProfileByUser(any(User.class))).thenReturn(DataHelper.getUserProfile(generateUser()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/profile")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetProfileNotFound () throws Exception {
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser());
        Mockito.when(userProfileService.getProfileByUser(any(User.class))).thenThrow(new UserProfileNotFoundException()).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/profile")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void testGetProfileByUser () throws Exception {
        Long userId = 20000L;
        User user = DataHelper.getRandomUser();
        user.setId(userId);
        Mockito.when(userService.getUser(anyLong())).thenReturn(user);
        Mockito.when(userProfileService.getProfileByUser(any(User.class))).thenReturn(DataHelper.getUserProfile(user));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/profile/id/"+20000)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetProfileByUserNotFound () throws Exception {
        Long userId = 20000L;
        User user = DataHelper.getRandomUser();
        user.setId(userId);
        Mockito.when(userService.getUser(anyLong())).thenReturn(user);
        Mockito.when(userProfileService.getProfileByUser(any(User.class))).thenThrow(new UserProfileNotFoundException()).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/profile/id/"+20000)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}
