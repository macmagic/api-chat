package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatParticipantNotFoundException;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.ChatParticipantKey;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.ChatParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatParticipantServiceImpl {

    private ChatParticipantRepository chatParticipantRepository;

    @Autowired
    public ChatParticipantServiceImpl(ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }

    public ChatParticipant addParticipantOnChat(Long chatId, Long userId) {
        return addParticipantOnChat(chatId, userId, false);
    }

    public ChatParticipant addParticipantOnChat(Long chatId, Long userId, boolean isAdmin) {

        ChatParticipant chatParticipant = new ChatParticipant();
        ChatParticipantKey chatParticipantKey = new ChatParticipantKey();
        chatParticipantKey.setChatId(chatId);
        chatParticipantKey.setUserId(userId);
        chatParticipant.setId(chatParticipantKey);
        chatParticipant.setAdmin(isAdmin);
        return chatParticipantRepository.save(chatParticipant);
    }

    public void addParticipantsOnChat(List<Long> users, Long chatId, User user) {

    }

    /**
     *
     * @param user
     * @param chatId
     * @return
     * @throws ChatParticipantNotFoundException
     */
    public ChatParticipant getParticipantInChat (User user, Long chatId) throws ChatParticipantNotFoundException {
        ChatParticipantKey idParticipant = new ChatParticipantKey();
        idParticipant.setChatId(chatId);
        idParticipant.setUserId(user.getId());

        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setId(idParticipant);

        Optional<ChatParticipant> result = chatParticipantRepository.findOne(Example.of(chatParticipant));

        if(!result.isPresent()) {
            throw new ChatParticipantNotFoundException("Cannot find participant with chat consulted");
        }
        return result.get();
    }



}
