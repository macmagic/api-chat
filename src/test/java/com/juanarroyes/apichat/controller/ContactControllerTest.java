package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.helpers.TokenHelper;
import com.juanarroyes.apichat.request.ContactBlockRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ContactControllerTest.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class ContactControllerTest extends AbstractControllerTest {

    @Before
    public void setUp() throws UserNotFoundException {
        //Mock for obtain user from token
        Mockito.when(userService.getUser(anyLong())).thenReturn(generateUser());
    }

    @Test
    public void testGetContacts() throws Exception {
        Mockito.when(contactListService.getContactsByUserId(anyLong())).thenReturn(DataHelper.getListOfContacts());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/contact")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetContact() throws Exception {
        Mockito.when(contactListService.getContactListByIdAndOwnerId(anyLong(), anyLong())).thenReturn(DataHelper.getContactList(DataHelper.getRandomUser(1000L),DataHelper.getRandomUser(1002L)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/contact/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testBlockContact() throws Exception {
        ContactBlockRequest contactBlockRequest = new ContactBlockRequest();
        contactBlockRequest.setContactId(1000L);
        Mockito.when(contactListService.blockContact(anyLong(), anyLong())).thenReturn(DataHelper.getContactList(DataHelper.getRandomUser(1000L),DataHelper.getRandomUser(1002L)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/contact/block")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(contactBlockRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testUnblockContact() throws Exception {
        ContactBlockRequest contactBlockRequest = new ContactBlockRequest();
        contactBlockRequest.setContactId(1000L);
        Mockito.when(contactListService.unblockContact(anyLong(), anyLong())).thenReturn(DataHelper.getContactList(DataHelper.getRandomUser(1000L),DataHelper.getRandomUser(1002L)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/contact/unblock")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(contactBlockRequest));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testDeleteContact() throws Exception {
        Mockito.when(contactListService.getContactListByIdAndOwnerId(anyLong(), anyLong())).thenReturn(DataHelper.getContactList(DataHelper.getRandomUser(1000L),DataHelper.getRandomUser(1002L)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/contact/1000")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TokenHelper.generateToken(USER_ID));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
