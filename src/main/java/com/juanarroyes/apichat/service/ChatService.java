package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatNotFoundException;
import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;

import java.util.List;

public interface ChatService {

    Chat createPrivateChat(User user, User userFriend) throws ContactListNotFoundException;

    Chat createRoomChat(Room room, User user, List<Long> users);

    Chat getChatById(Long chatId) throws ChatNotFoundException;

    Chat getChatByRoom(Room room) throws ChatNotFoundException;

    Chat getPrivateChatByUserAndFriend(User user, User userFriend) throws ChatNotFoundException;

    Chat getChatByIdAndUser(Long chatId, User user) throws ChatNotFoundException;
}
