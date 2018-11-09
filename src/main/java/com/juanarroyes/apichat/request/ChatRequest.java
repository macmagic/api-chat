package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.juanarroyes.apichat.repository.ChatRepository;

public class ChatRequest {

    @JsonAlias("user_id")
    private Long userId;

    public ChatRequest () {

    }

    public ChatRequest (Long userId) {
        this.userId = userId;
    }

    public Long getUserId () {
        return userId;
    }

    public void setUserId (Long userId) {
        this.userId = userId;
    }


}
