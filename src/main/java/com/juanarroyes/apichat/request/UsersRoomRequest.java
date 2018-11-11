package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class UsersRoomRequest {

    @JsonAlias("chat_room_id")
    private Long chatRoomId;

    private List<Long> users;

    public UsersRoomRequest() {

    }

    public UsersRoomRequest(Long chatRoomId, List<Long> users) {
        this.chatRoomId = chatRoomId;
        this.users = users;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long roomId) {
        this.chatRoomId = roomId;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}
