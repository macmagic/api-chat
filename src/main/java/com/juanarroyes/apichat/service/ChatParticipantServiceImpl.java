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
public class ChatParticipantServiceImpl implements ChatParticipantService {

    private ChatParticipantRepository chatParticipantRepository;

    @Autowired
    public ChatParticipantServiceImpl(ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }

    /**
     * Add user on chat to make participant
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chatId Chat id to user is added
     * @param userId user id to add in chat
     * @return ChatParticipant Entity information for relation created with chat and user
     */
    @Override
    public ChatParticipant addParticipantOnChat(Long chatId, Long userId) {
        return addParticipantOnChat(chatId, userId, false);
    }

    /**
     * Add user on chat with rol
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chatId Chat id to user is added
     * @param userId User id to add in chat
     * @param isAdmin Set the rol of participant, admin or not
     * @return ChatParticipant Entity information for relation created with chat and user
     */
    @Override
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
     * Add users to chat when room chat is new
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chat Chat target to add new users
     * @param users List of users to add in chat
     */
    @Override
    public void addParticipantsOnChat(Chat chat, List<Long> users) {
        createParticipantsOnChat(chat.getId(), users);
    }

    /**
     * Add users to chat invited by user admin on room chat
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chat Chat target to add new users
     * @param users List of users to add in chat
     * @param user User to invite the new users to chat
     */
    @Override
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
     * Get relation with user and chat, if no exists, return exception with ChatParticipantNotFoundException
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param user User with find is in chat
     * @param chatId Chat target to find user
     * @return ChatParticipant entity with relation data.
     * @throws ChatParticipantNotFoundException When relation for chat and user is not found, exception.
     */
    @Override
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
    @Override
    public ChatParticipant updateParticipantRol(Chat chat, User user, Boolean admin)throws ChatParticipantNotFoundException {

        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), user.getId());
        Optional<ChatParticipant> result = chatParticipantRepository.findById(id);
        if(result.isPresent()) {
            throw new ChatParticipantNotFoundException("Cannot find participant on this chat");
        }

        ChatParticipant participant = result.get();
        participant.setAdmin(admin);
        return chatParticipantRepository.save(participant);
    }

    /**
     *
     * @param chat
     * @param users
     */
    @Override
    public void deleteParticipantsOnChat(Chat chat, List<Long> users, User user) throws ChatParticipantNotFoundException, UserNotAllowedException{

        if(users == null) {
            return;
        }

        ChatParticipantKey participantId = new ChatParticipantKey(chat.getId(), user.getId());
        Optional<ChatParticipant> result = chatParticipantRepository.findById(participantId);

        if(!result.isPresent()) {
            throw new ChatParticipantNotFoundException("Cannot find participant relation for user " + user.getId());
        } else if(!result.get().isAdmin()) {
            throw new UserNotAllowedException("User cannot make this action on this room");
        }

        for(Long userId : users) {
            ChatParticipantKey id = new ChatParticipantKey();
            id.setChatId(chat.getId());
            id.setUserId(userId);
            chatParticipantRepository.deleteById(id);
        }
    }

    @Override
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
