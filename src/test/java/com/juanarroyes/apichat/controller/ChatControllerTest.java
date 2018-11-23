package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.ChatNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.ChatRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ChatController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class ChatControllerTest extends AbstractControllerTest {

    public ChatControllerTest() {
        authRequired = true;
    }

    @Test
    public void testCreatePrivateChat() throws Exception {
        Chat mockChat = DataHelper.getChatPrivate(2200L);
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUserId(mockChat.getId());
        Mockito.when(chatService.createPrivateChat(any(User.class), any(User.class))).thenReturn(mockChat);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/chat")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(DataHelper.getStaticUserId()))
                .content(objectMapper.writeValueAsString(chatRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void testGetChat() throws Exception {
        Mockito.when(chatService.getChatById(anyLong())).thenReturn(DataHelper.getChatPrivate());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chat/"+2000)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(DataHelper.getStaticUserId()));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetChatNotFound() throws Exception {
        Mockito.when(chatService.getChatById(anyLong())).thenThrow(new ChatNotFoundException()).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chat/"+2000)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(DataHelper.getStaticUserId()));
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void testGetChatsByUser() throws Exception {
        List<Chat> chatList = DataHelper.getListOfChats();
        Mockito.when(chatService.getChatsByUser(any(User.class))).thenReturn(chatList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/chat/"+2100)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(DataHelper.getStaticUserId()));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
