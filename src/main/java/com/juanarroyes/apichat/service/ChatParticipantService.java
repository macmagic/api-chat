package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatParticipantNotFoundException;
import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.User;

import java.util.List;

interface ChatParticipantService {

    ChatParticipant addParticipantOnChat(Chat chat, User user);

    ChatParticipant addParticipantOnChat(Chat chat, User user, boolean isAdmin);

    void addParticipantsOnChat(Chat chat, List<Long> users);

    void addParticipantsOnChat(Chat chat, List<Long> users, User user) throws ChatParticipantNotFoundException, UserNotAllowedException;

    List<ChatParticipant> getListOfParticipantsInChat (Chat chat);

    ChatParticipant getParticipantInChat (User user, Chat chat) throws ChatParticipantNotFoundException;

    ChatParticipant updateParticipantRol(Chat chat, User user, Boolean admin)throws ChatParticipantNotFoundException;

    void deleteParticipantsOnChat(Chat chat, List<Long> users, User user) throws UserNotAllowedException;

    void leaveUserFromChat(Chat chat, User user);

    void kickUserFromChat(Chat chat, User userKicked, User user) throws UserNotAllowedException;

    boolean isUserAdmin(Chat chat, User user);

    void deleteAllParticipantsFromChat(Chat chat);
}
