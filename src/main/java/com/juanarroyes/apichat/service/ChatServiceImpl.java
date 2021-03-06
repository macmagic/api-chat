package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.*;
import com.juanarroyes.apichat.model.*;
import com.juanarroyes.apichat.repository.ChatRepository;
import com.juanarroyes.apichat.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private static final int SESSION_LENGTH = 40;

    private ChatRepository chatRepository;
    private ChatParticipantService chatParticipantService;
    private ContactListService contactListService;
    private RoomService roomService;

    public ChatServiceImpl(ChatRepository chatRepository, ChatParticipantServiceImpl chatParticipantService, ContactListService contactListService, RoomService roomService) {
        this.chatRepository = chatRepository;
        this.chatParticipantService = chatParticipantService;
        this.contactListService = contactListService;
        this.roomService = roomService;
    }

    /**
     * Create private chat with two users
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param user User with create a private chat
     * @param userFriend User friend who user is creating the chat
     * @return Chat entity with information of chat data
     */
    @Transactional
    @Override
    public Chat createPrivateChat(User user, User userFriend) throws ContactListNotFoundException {

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
        Chat chat = chatRepository.save(newChat);
        chatParticipantService.addParticipantOnChat(chat, user);
        chatParticipantService.addParticipantOnChat(chat, userFriend);
        return chat;
    }

    /**
     * Create a chat room with list of users
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param room Entity with room data
     * @param users List of users to add in the room
     * @return Chat entity with data
     */
    @Override
    public Chat createRoomChat(Room room, User user, List<Long> users) {
        Chat chat = new Chat();
        chat.setSessionId(generateSessionId());
        chat.setIsRoom(true);
        chat.setPrivate(false);
        chat.setRoom(room);
        chat = chatRepository.save(chat);

        chatParticipantService.addParticipantOnChat(chat, user, true);

        if(users != null) {
            chatParticipantService.addParticipantsOnChat(chat, users);
        }
        return chat;
    }

    /**
     * Get chat by chat id
     *
     * @author jarroyes
     * @since 2018-10-30
     *
     * @param chatId Chat id to get the info
     * @return Chat entity with data
     * @throws ChatNotFoundException Exception if chat not found
     */
    @Override
    public Chat getChatById(Long chatId) throws ChatNotFoundException {
        Optional<Chat> result = chatRepository.findById(chatId);
        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find chat: " + chatId);
        }
        return result.get();
    }

    /**
     * Get private chat data with user and user friend data
     *
     * @param user User to request the chat
     * @param userFriend Friend with is in on chat instance
     * @return Chat entity with data
     * @throws ChatNotFoundException Exception if chat is not found
     */
    @Override
    public Chat getPrivateChatByUserAndFriend(User user, User userFriend) throws ChatNotFoundException {
        List<Long> users = Arrays.asList(user.getId(), userFriend.getId());
        Optional<Chat> result = chatRepository.findByPrivateChatByUsers(users);
        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find private chat for user: " + user.getId() + " and friend: " + userFriend.getId());
        }
        return result.get();
    }

    @Override
    public Chat getChatByIdAndUser(Long chatId, User user) throws ChatNotFoundException {
        Optional<Chat> result = chatRepository.findByChatAndUser(chatId, user.getId());

        if(!result.isPresent()) {
            throw new ChatNotFoundException("Cannot find private chat for chat_id: " + chatId + " and user " + user.getId());
        }
        return result.get();
    }

    @Override
    public List<Chat> getChatsByUser(User user) {
        return chatRepository.findAllByUser(user.getId());
    }

    @Override
    public List<Chat> getChatRoomsByUser(User user) {
        return chatRepository.findAllChatRoomsByUser(user.getId());
    }

    public void deleteChatRoom(Chat chat) {
        chatParticipantService.deleteAllParticipantsFromChat(chat);
        chatRepository.delete(chat);
        roomService.deleteRoomInfo(chat.getRoom());
    }

    private String generateSessionId() {
        return Utils.generateRandomString(SESSION_LENGTH);
    }
}
