package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatParticipantNotFoundException;
import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.User;

import java.util.List;

interface ChatParticipantService {

    ChatParticipant addParticipantOnChat(Long chatId, Long userId);

    ChatParticipant addParticipantOnChat(Long chatId, Long userId, boolean isAdmin);

    void addParticipantsOnChat(Chat chat, List<Long> users);

    void addParticipantsOnChat(Chat chat, List<Long> users, User user) throws ChatParticipantNotFoundException, UserNotAllowedException;

    ChatParticipant getParticipantInChat (User user, Long chatId) throws ChatParticipantNotFoundException;

    ChatParticipant updateParticipantRol(Chat chat, User user, Boolean admin)throws ChatParticipantNotFoundException;

    void deleteParticipantsOnChat(Chat chat, List<Long> users, User user) throws ChatParticipantNotFoundException, UserNotAllowedException;

    void leaveUserFromChat(Chat chat, User user);
}
