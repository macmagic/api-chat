package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatAlreadyExistsException;
import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.ChatRepository;
import com.juanarroyes.apichat.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param userId
     * @return
     */
    @Transactional
    public Chat createPrivateChat(User user, Long userId) throws ContactListNotFoundException, UserNotFoundException, ChatAlreadyExistsException{

        User userFriend = userService.getUser(userId);

        ContactList contactList = contactListService.getContactByOwnerUserAndFriend(userFriend, user);
        if(contactList == null) {
            throw new ContactListNotFoundException("Cannot found contact");
        }

        List<Long> users = Arrays.asList(user.getId(), userFriend.getId());

        Optional<Chat> resultCheck = chatRepository.findByPrivateChatByUsers(users);

        if(resultCheck.isPresent()) {
            throw new ChatAlreadyExistsException("Chat already exists!");
        }

        Chat newChat = new Chat();
        newChat.setIsRoom(false);
        newChat.setSessionId(generateSessionId());
        newChat.setPrivate(true);
        Chat result = chatRepository.save(newChat);
        chatParticipantService.addParticipantOnChat(result.getId(), user.getId());
        chatParticipantService.addParticipantOnChat(result.getId(), userId);
        return result;
    }



    private String generateSessionId() {
        return Utils.generateRandomString(SESSION_LENGTH);
    }

}
