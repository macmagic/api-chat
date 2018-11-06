package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class UsersRoomRequest {

    @JsonAlias("room_id")
    private Long roomId;

    private List<Long> users;

    public UsersRoomRequest() {

    }

    public UsersRoomRequest(Long roomId, List<Long> users) {
        this.roomId = roomId;
        this.users = users;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}
