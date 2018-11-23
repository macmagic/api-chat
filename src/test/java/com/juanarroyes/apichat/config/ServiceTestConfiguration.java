package com.juanarroyes.apichat.config;

import com.juanarroyes.apichat.service.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.parameters.P;

@Configuration
@Profile("test")
public class ServiceTestConfiguration {

    @Bean
    @Primary
    public UserServiceImpl userService() {
        return Mockito.mock(UserServiceImpl.class);
    }

    @Bean
    @Primary
    public TokenServiceImpl tokenService() {
        return Mockito.mock(TokenServiceImpl.class);
    }

    @Bean
    @Primary
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    @Primary
    public UserProfileServiceImpl userProfileService() {
        return Mockito.mock(UserProfileServiceImpl.class);
    }

    @Bean
    @Primary
    public UserRequestServiceImpl userRequestService() {
        return Mockito.mock(UserRequestServiceImpl.class);
    }

    @Bean
    @Primary
    public ChatServiceImpl chatService() {
        return Mockito.mock(ChatServiceImpl.class);
    }

    @Bean
    @Primary
    public ChatParticipantServiceImpl chatParticipantService() {
        return Mockito.mock(ChatParticipantServiceImpl.class);
    }

    @Bean
    @Primary
    public ContactListServiceImpl contactListService() {
        return Mockito.mock(ContactListServiceImpl.class);
    }

    @Bean
    @Primary
    public MessageServiceImpl messageService() {
        return Mockito.mock(MessageServiceImpl.class);
    }

    @Bean
    @Primary
    public RoomServiceImpl roomService() {
        return Mockito.mock(RoomServiceImpl.class);
    }
}
