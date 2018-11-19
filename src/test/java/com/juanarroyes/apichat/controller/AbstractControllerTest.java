package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.*;
import com.juanarroyes.apichat.repository.*;
import com.juanarroyes.apichat.security.UserPrincipal;
import com.juanarroyes.apichat.service.*;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

public abstract class AbstractControllerTest {

    protected static final Long USER_ID = 10000L;
    protected static final String USER_EMAIL = "testuser@example.com";
    protected static final String USER_PASSWORD_RAW = "1234password";
    protected static final String USER_PASSWORD = "$31$16$GmHTpJldiXpj5PpjCYSFuGAmde2DShDQoepigiENokA";

    protected static final String USER_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTQyNjE4NDg0LCJleHAiOjE1NDI3MDQ4ODR9.OsywpD604kFhy-qRiJkw5mMw-94DY7Qn3WNmm93TOVcBYhP7R5gzmI_jdvUHJ_x3f1K693tUCjhXOshrBZiIgQ";
    protected static final Long USER_TOKEN_EXPIRATION = 86400000L;
    protected static final String USER_REFRESH_TOKEN = "f76224c9-643c-47ba-a416-d6cb01305960";

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
    protected CustomUserDetailsService customUserDetailsService;

    @Autowired
    protected TokenServiceImpl tokenService;

    @Autowired
    protected UserServiceImpl userService;

    @Autowired
    protected UserRequestServiceImpl userRequestService;

    @Autowired
    protected UserProfileServiceImpl userProfileService;

    @Autowired
    protected ContactListServiceImpl contactListService;

    @Autowired
    protected ChatServiceImpl chatService;

    @Autowired
    protected ChatParticipantServiceImpl chatParticipantService;

    @Autowired
    protected MessageServiceImpl messageService;

    @Autowired
    protected RoomServiceImpl roomService;

    @Before
    public void setUpMocks() throws UserAlreadyExistException {

        Mockito.when(customUserDetailsService.loadUserById(anyLong())).thenReturn(UserPrincipal.create(generateUser()));

        Mockito.when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(UserPrincipal.create(generateUser()));

        Mockito.when(userService.existsByEmail(anyString())).thenReturn(false);

        Mockito.when(userService.createUser(anyString(), anyString())).thenReturn(generateUser());

        Mockito.when(tokenService.generateToken(any(UserPrincipal.class))).thenReturn(USER_TOKEN);

        Mockito.when(tokenService.generateRefreshToken()).thenReturn(USER_REFRESH_TOKEN);

        Mockito.when(tokenService.validateToken(anyString())).thenReturn(true);

        Mockito.when(tokenService.getUserIdByToken(anyString())).thenReturn(USER_ID);
    }

    /*private void setUpMocksAuth() throws UserAlreadyExistException{
        Mockito.when(customUserDetailsService.loadUserById(anyLong())).thenReturn(UserPrincipal.create(generateUser()));

        Mockito.when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(UserPrincipal.create(generateUser()));

        Mockito.when(userService.existsByEmail(anyString())).thenReturn(false);

        Mockito.when(userService.createUser(anyString(), anyString())).thenReturn(generateUser());

        Mockito.when(tokenService.generateToken(any(UserPrincipal.class))).thenReturn(USER_TOKEN);

        Mockito.when(tokenService.generateRefreshToken()).thenReturn(USER_REFRESH_TOKEN);
    }*/

    private static Optional<UserProfile> getUserProfile() {
        User user = DataHelper.getRandomUser();
        user.setId(USER_ID);
        return Optional.of(DataHelper.getUserProfile(user));
    }

    public static Optional<User> getUser() {
        return Optional.of(generateUser());
    }

    public static User generateUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setStatus(1);
        user.setCreated(new Date());
        return user;
    }
}
