package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.ChatRepository;
import com.juanarroyes.apichat.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class ChatServiceImpl {

    private final static int SESSION_LENGTH = 40;

    private ChatRepository chatRepository;
    private ChatParticipantServiceImpl chatParticipantService;

    public ChatServiceImpl(ChatRepository chatRepository, ChatParticipantServiceImpl chatParticipantService) {
        this.chatRepository = chatRepository;
        this.chatParticipantService = chatParticipantService;
    }

    /**
     *
     * @param user
     * @param userId
     * @return
     */
    @Transactional
    public Chat createPrivateChat(User user, Long userId) {
        Chat newChat = new Chat();
        newChat.setIsRoom(false);
        newChat.setSessionId(generateSessionId());
        Chat result = chatRepository.save(newChat);
        chatParticipantService.addParticipantOnChat(result.getId(), user.getId());
        chatParticipantService.addParticipantOnChat(result.getId(), userId);
        return result;
    }

    private String generateSessionId() {
        return Utils.generateRandomString(SESSION_LENGTH);
    }

}
