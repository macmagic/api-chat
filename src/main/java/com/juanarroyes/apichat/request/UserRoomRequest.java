package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UserRoomRequest {

    @JsonAlias("user_id")
    private Long userId;

    @JsonAlias("chat_room_id")
    private Long chatRoomId;

    @JsonAlias("admin")
    private Boolean admin;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
