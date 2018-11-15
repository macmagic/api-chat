package com.juanarroyes.apichat.controller;

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
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public abstract class AbstractControllerTest {

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

    @Before
    public void setUpMocks () {
        //userProfileRepository.findByUserId
        Mockito.when(userProfileRepository.findByUserId(any(UserProfileKey.class))).thenReturn(getUserProfile());
    }

    private static Optional<UserProfile> getUserProfile() {
        User user = DataHelper.getRandomUser();
        user.setId(10000L);
        return Optional.of(DataHelper.getUserProfile(user));
    }
}
