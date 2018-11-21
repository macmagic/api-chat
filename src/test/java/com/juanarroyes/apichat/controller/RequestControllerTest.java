package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;
import com.juanarroyes.apichat.request.UserAnswerRequest;
import com.juanarroyes.apichat.request.UserRequestRequest;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {RequestControllerTest.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class RequestControllerTest extends AbstractControllerTest {

    public RequestControllerTest() {
        authRequired =true;
    }

    /*
    @Before
    public void setUp() throws UserNotFoundException {
        //Mock for obtain user from token
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser());
    }*/

    @Test
    public void testGetRequest () throws Exception {
        List<UserRequest> userRequestList = new ArrayList<>();
        userRequestList.add(DataHelper.getUserRequest(DataHelper.getRandomUser(), DataHelper.getRandomUser()));
        Mockito.when(userRequestService.getAllRequestByUser(any(User.class))).thenReturn(userRequestList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/request")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testCreateRequest() throws Exception {
        UserRequestRequest userRequestRequest = new UserRequestRequest();
        User user = DataHelper.getRandomUser();
        user.setId(20000L);
        userRequestRequest.setUserId(user.getId());
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser(), DataHelper.getRandomUser());
        Mockito.when(userRequestService.createRequest(any(User.class), any(User.class))).thenReturn(DataHelper.getUserRequest(user, generateUser()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/request")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequestRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testAnswerRequest() throws Exception {
        UserAnswerRequest userAnswerRequest = new UserAnswerRequest();
        userAnswerRequest.setRequestId(20000L);
        userAnswerRequest.setUserRequestResponse(UserRequest.REQUEST_ALLOW);

        Mockito.when(userRequestService.answerRequest(anyString(), anyLong(), any(User.class))).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/request")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userAnswerRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testDeleteRequest() throws Exception {

        UserRequest userRequest = new UserRequest();
        userRequest.setUser(generateUser());
        userRequest.setUserRequest(DataHelper.getRandomUser());

        Mockito.when(userRequestService.getRequest(anyLong())).thenReturn(userRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/request/20000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .content("{ \"requestId\": \"22222\" }")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

}
