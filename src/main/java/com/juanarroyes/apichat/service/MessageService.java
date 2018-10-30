package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.model.User;

import java.util.List;

public interface MessageService {

    List<Message> getMessagesByChat(Chat chat);

    Message sendMessage(User user, Chat chat, String messageText, Integer messageType);

    Message sendMessage(User user, Chat chat, String messageText, Integer messageType, String attachUrl);
}
