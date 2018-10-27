package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.*;
import com.juanarroyes.apichat.model.*;
import com.juanarroyes.apichat.repository.ChatRepository;
import com.juanarroyes.apichat.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Service
public class ChatServiceImpl {

    private final static int SESSION_LENGTH = 40;

    private ChatRepository chatRepository;
    private ChatParticipantServiceImpl chatParticipantService;
    private ContactListService contactListService;
    private UserService userService;

    public ChatServiceImpl(ChatRepository chatRepository, ChatParticipantServiceImpl chatParticipantService, ContactListService contactListService, UserService userService) {
        this.chatRepository = chatRepository;
        this.chatParticipantService = chatParticipantService;
        this.contactListService = contactListService;
        this.userService = userService;
    }

    /**
     *
     * @param user
     * @param userFriend
     * @return
     */
    @Transactional
    public Chat createPrivateChat(User user, User userFriend) throws ContactListNotFoundException, UserNotFoundException, ChatAlreadyExistsException{

        ContactList contactList = contactListService.getContactByOwnerUserAndFriend(userFriend, user);
        if(contactList == null) {
            throw new ContactListNotFoundException("Cannot found contact");
        }

        List<Long> users = Arrays.asList(user.getId(), userFriend.getId());

        Optional<Chat> resultCheck = chatRepository.findByPrivateChatByUsers(users);

        if(resultCheck.isPresent()) {
            return resultCheck.get();
        }

        Chat newChat = new Chat();
        newChat.setIsRoom(false);
        newChat.setSessionId(generateSessionId());
        newChat.setPrivate(true);
        Chat result = chatRepository.save(newChat);
        chatParticipantService.addParticipantOnChat(result.getId(), user.getId());
        chatParticipantService.addParticipantOnChat(result.getId(), user.getId());
        return result;
    }

    /**
     *
     * @param room
     * @param users
     * @return
     */
    public Chat createRoomChat(Room room, List<Long> users) {
        Chat chat = new Chat();
        chat.setSessionId(generateSessionId());
        chat.setIsRoom(true);
        chat.setPrivate(false);
        chat.setRoomId(room.getId());
        chat = chatRepository.save(chat);

        if(users != null) {
            chatParticipantService.addParticipantsOnChat(chat, users);
        }
        return chat;
    }

    /**
     *
     * @param chatId
     * @return
     * @throws ChatNotFoundException
     */
    public Chat getChatById(Long chatId) throws ChatNotFoundException {
        Optional<Chat> result = chatRepository.findById(chatId);
        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find chat: " + chatId);
        }
        return result.get();
    }

    public Chat getChatByRoom(Room room) throws ChatNotFoundException {
        Optional<Chat> result = chatRepository.findByRoomId(room.getId());
        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find chat for this room");
        }
        return result.get();
    }

    /**
     *
     * @param user
     * @param userFriend
     * @return
     * @throws ChatNotFoundException
     */
    public Chat getPrivateChatByUserAndFriend(User user, User userFriend) throws ChatNotFoundException {
        List<Long> users = Arrays.asList(user.getId(), userFriend.getId());
        Optional<Chat> result = chatRepository.findByPrivateChatByUsers(users);
        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find private chat for user: " + user.getId() + " and friend: " + userFriend.getId());
        }
        return result.get();
    }

    private String generateSessionId() {
        return Utils.generateRandomString(SESSION_LENGTH);
    }

    public Chat getChatByIdAndUser(Long chatId, User user) throws ChatNotFoundException {
        Optional<Chat> result = chatRepository.findByChatAndUser(chatId, user.getId());

        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find private chat for chat_id: " + chatId + " and user " + user.getId());
        }
        return result.get();
    }

}
