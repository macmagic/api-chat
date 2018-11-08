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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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
     * @param chat Chat id to user is added
     * @param user user id to add in chat
     * @return ChatParticipant Entity information for relation created with chat and user
     */
    @Override
    public ChatParticipant addParticipantOnChat(Chat chat, User user) {
        return addParticipantOnChat(chat, user, false);
    }

    /**
     * Add user on chat with rol
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chat Chat id to user is added
     * @param user User id to add in chat
     * @param isAdmin Set the rol of participant, admin or not
     * @return ChatParticipant Entity information for relation created with chat and user
     */
    @Override
    public ChatParticipant addParticipantOnChat(Chat chat, User user, boolean isAdmin) {

        ChatParticipant chatParticipant = new ChatParticipant();
        ChatParticipantKey chatParticipantKey = new ChatParticipantKey();
        chatParticipantKey.setChatId(chat.getId());
        chatParticipantKey.setUserId(user.getId());
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
            ChatParticipant chatParticipant = getParticipantInChat(user, chat);
            if(!chatParticipant.isAdmin()) {
                throw new UserNotAllowedException("User is not admin to add participants on chat");
            }
        }
        createParticipantsOnChat(chat.getId(), users);
    }

    public List<ChatParticipant> getListOfParticipantsInChat (Chat chat) {
        return chatParticipantRepository.findAllByChat(chat.getId());
    }

    /**
     * Get relation with user and chat, if no exists, return exception with ChatParticipantNotFoundException
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param user User with find is in chat
     * @param chat Chat target to find user
     * @return ChatParticipant entity with relation data.
     * @throws ChatParticipantNotFoundException When relation for chat and user is not found, exception.
     */
    @Override
    public ChatParticipant getParticipantInChat (User user, Chat chat) throws ChatParticipantNotFoundException {
        ChatParticipantKey idParticipant = new ChatParticipantKey(chat.getId(), user.getId());
        Optional<ChatParticipant> result = chatParticipantRepository.findById(idParticipant);

        if(!result.isPresent()) {
            throw new ChatParticipantNotFoundException("Cannot find participant with chat consulted");
        }
        return result.get();
    }

    /**
     * Update user on chat to admin o remove the admin rol
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chat Chat target to update the rol
     * @param user User to change the rol
     * @return ChatParticipant entity wiith relation data
     */
    @Override
    public ChatParticipant updateParticipantRol(Chat chat, User user, Boolean admin) throws ChatParticipantNotFoundException {

        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), user.getId());
        Optional<ChatParticipant> result = chatParticipantRepository.findById(id);
        if(!result.isPresent()) {
            throw new ChatParticipantNotFoundException("Cannot find participant on this chat");
        }

        ChatParticipant participant = result.get();
        participant.setAdmin(admin);
        return chatParticipantRepository.save(participant);
    }

    /**
     * Delete participants on chat
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chat Chat target with delete the users.
     * @param users List of users to delete.
     * @param user User to make the deletion, the user need have a admin rol.
     */
    @Override
    public void deleteParticipantsOnChat(Chat chat, List<Long> users, User user) throws UserNotAllowedException{

        if(users == null) {
            return;
        }

        if(!isUserAdmin(chat, user)) {
            throw new UserNotAllowedException("User cannot make this action on this room");
        }

        for(Long userId : users) {
            ChatParticipantKey id = new ChatParticipantKey();
            id.setChatId(chat.getId());
            id.setUserId(userId);
            chatParticipantRepository.deleteById(id);
        }
    }

    /**
     * Leave user from chat room
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chat Chat target to user leave
     * @param user User to leave
     */
    @Override
    public void leaveUserFromChat(Chat chat, User user) {
        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), user.getId());
        chatParticipantRepository.deleteById(id);
        if(!checkIfExistsAnotherAdminOnRoomChat(chat)) {
            addRandomAdminOnRoomChat(chat);
        }
    }

    @Override
    public void kickUserFromChat(Chat chat, User userKicked, User user) throws UserNotAllowedException{
        if(!isUserAdmin(chat, user)) {
            throw new UserNotAllowedException("User cannot make this action on this room");
        }

        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), userKicked.getId());
        chatParticipantRepository.deleteById(id);
    }

    @Override
    public boolean isUserAdmin(Chat chat, User user) {
        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), user.getId());
        Optional<ChatParticipant> result = chatParticipantRepository.findById(id);

        return result.isPresent() && result.get().isAdmin();
    }

    /**
     * Create relation in chat with list of users
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chatId Chat id to make relation
     * @param users List of users to create relation in chat
     */
    private void createParticipantsOnChat (Long chatId, List<Long> users) {
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

    /**
     * Check if exists a one admin on room chat
     *
     * @author jarroyes
     * @since 2018-11-07
     *
     * @param chat Chat object with check admin
     * @return boolean return true if exists or false if not
     */
    private boolean checkIfExistsAnotherAdminOnRoomChat (Chat chat) {
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByChatAndAdmin(chat.getId(), true);
        return !chatParticipants.isEmpty();
    }

    /**
     * Set one user random from room to admin
     *
     * @author jarroyes
     * @since 2018-11-07
     *
     * @param chat Chat object to set a admin
     */
    private void addRandomAdminOnRoomChat(Chat chat) {
        List<ChatParticipant> listParticipants = chatParticipantRepository.findAllByChat(chat.getId());
        int keyRandom = new Random().nextInt(listParticipants.size());
        ChatParticipant participant = listParticipants.get(keyRandom);
        participant.setAdmin(true);
        chatParticipantRepository.save(participant);
    }
}
