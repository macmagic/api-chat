package com.juanarroyes.apichat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.*;
import com.juanarroyes.apichat.repository.*;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public abstract class AbstractControllerTest {

    protected static final String USER_EMAIL = "testuser@example.com";
    protected static final String USER_PASSWORD = "1234password";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ChatRepository chatRepository;

    @MockBean
    protected ChatParticipantRepository chatParticipantRepository;

    @MockBean
    protected ContactListRepository contactListRepository;

    @MockBean
    protected ContactListStatusRepository contactListStatusRepository;

    @MockBean
    protected MessageRepository messageRepository;

    @MockBean
    protected MessageTypeRepository messageTypeRepository;

    @MockBean
    protected RoomRepository roomRepository;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected UserProfileRepository userProfileRepository;

    @MockBean
    protected UserRequestRepository userRequestRepository;

    @MockBean
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected TokenService tokenService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Before
    public void setUpMocks () throws UserAlreadyExistException {
        //userProfileRepository.findByUserId
        Mockito.when(userProfileRepository.findByUserId(any(UserProfileKey.class))).thenReturn(getUserProfile());

        Mockito.when(userRepository.save(any(User.class))).thenReturn(generateUser());

        //userRepository.findByEmail
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        //Mockito.when(userService.createUser(Mockito.any(), Mockito.any())).thenReturn(DataHelper.getRandomUser());
    }

    private static Optional<UserProfile> getUserProfile() {
        User user = DataHelper.getRandomUser();
        user.setId(10000L);
        return Optional.of(DataHelper.getUserProfile(user));
    }

    public static User generateUser() {
        User user = new User();
        user.setId(1000L);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setStatus(1);
        user.setCreated(new Date());
        return user;
    }
}
