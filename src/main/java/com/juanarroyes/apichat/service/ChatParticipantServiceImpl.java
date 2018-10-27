package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatParticipantNotFoundException;
import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.model.Chat;
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

    /**
     *
     * @param chat
     * @param users
     * @return
     */
    public void addParticipantsOnChat(Chat chat, List<Long> users) {
        createParticipantsOnChat(chat.getId(), users);
    }

    /**
     *
     * @param chat
     * @param users
     * @param user
     * @return
     */
    public void addParticipantsOnChat(Chat chat, List<Long> users, User user) throws ChatParticipantNotFoundException, UserNotAllowedException {

        if(user != null) {
            ChatParticipant chatParticipant = getParticipantInChat(user, chat.getId());
            if(!chatParticipant.isAdmin()) {
                throw new UserNotAllowedException("User is not admin to add participants on chat");
            }
        }
        createParticipantsOnChat(chat.getId(), users);
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

    /**
     *
     * @return
     */
    public ChatParticipant updateParticipantRol() {
        return null;
    }

    /**
     *
     * @param chat
     * @param users
     */
    public void deleteParticipantsOnChat(Chat chat, List<Long> users) {
        if(users == null) {
            return;
        }

        for(Long userId : users) {
            ChatParticipantKey id = new ChatParticipantKey();
            id.setChatId(chat.getId());
            id.setUserId(userId);
            chatParticipantRepository.deleteById(id);
        }
    }

    public void leaveUserFromChat(Chat chat, User user) {
        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), user.getId());
        chatParticipantRepository.deleteById(id);
    }

    /**
     *
     * @param chatId
     * @param users
     */
    private void createParticipantsOnChat(Long chatId, List<Long> users) {
        if(users == null) {
            return;
        }

        for(Long userId : users) {
            ChatParticipant participant = new ChatParticipant();
            ChatParticipantKey key = new ChatParticipantKey();
            key.setChatId(chatId);
            key.setUserId(userId);
            participant.setId(key);
            participant.setAdmin(false);
            chatParticipantRepository.save(participant);
        }
    }

}
