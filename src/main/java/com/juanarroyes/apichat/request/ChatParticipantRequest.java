package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class ChatParticipantRequest {

    private List<Long> users;

    @JsonAlias(value = "chat_id")
    private Long chatId;

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
