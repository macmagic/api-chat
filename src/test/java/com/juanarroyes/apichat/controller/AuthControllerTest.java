package com.juanarroyes.apichat.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.security.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {AuthController.class}, secure = false)
@ComponentScan("com.juanarroyes.apichat")
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractControllerTest {

    /*@Test(expected = JsonMappingException.class)
    public void testRegisterUser() throws Exception {




        User appUser = generateUser();
        UserPrincipal userPrincipal = UserPrincipal.create(appUser);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);


        //Mockito.when(tokenService.generateToken(userPrincipal)).thenReturn()
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \""+USER_EMAIL+"\", \"password\":\""+USER_PASSWORD+"\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }*/

}
