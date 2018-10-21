package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.ChatParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatParticipantServiceImpl {

    private ChatParticipantRepository chatParticipantRepository;

    @Autowired
    public ChatParticipantServiceImpl(ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }

    public void addParticipantsOnChat(List<Long> users, Long chatId, User user) {

    }

    public ChatParticipant getParticipantInChat (User user, Long chatId) {
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatIdAndUserId(chatId, user.getId());
        return chatParticipant;
    }



}
