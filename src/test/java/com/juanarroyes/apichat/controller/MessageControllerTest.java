package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.request.MessageRequest;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {MessageControllerTest.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class MessageControllerTest extends AbstractControllerTest {

    public MessageControllerTest() {
        authRequired = true;
    }

    /*@Before
    public void setUp() throws UserNotFoundException {
        //Mock for obtain user from token
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser());
    }*/

    @Test
    public void testGetMessageByChat() throws Exception {
        List<Message> messageList = new ArrayList<>();
        Mockito.when(chatService.getChatByIdAndUser(anyLong(), any(User.class))).thenReturn(DataHelper.getChatPrivate());
        messageList.add(DataHelper.getRandomMessage(DataHelper.getChatPrivate(), DataHelper.getRandomUser()));
        messageList.add(DataHelper.getRandomMessage(DataHelper.getChatPrivate(), DataHelper.getRandomUser()));
        messageList.add(DataHelper.getRandomMessage(DataHelper.getChatPrivate(), DataHelper.getRandomUser()));
        Mockito.when(messageService.getMessagesByChat(any(Chat.class))).thenReturn(messageList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/message/chat/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testSendMessage() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageText("TEST");
        messageRequest.setChatId(1000L);
        messageRequest.setMessageType(1);
        Mockito.when(chatService.getChatByIdAndUser(anyLong(), any(User.class))).thenReturn(DataHelper.getChatPrivate(1000L));
        Message message = DataHelper.getRandomMessage(DataHelper.getChatPrivate(1000L), DataHelper.getRandomUser(1000L), 1000L);
        Mockito.when(messageService.sendMessage(any(User.class), any(Chat.class), anyString(), anyInt(), anyString())).thenReturn(message);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/message/send")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(messageRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
