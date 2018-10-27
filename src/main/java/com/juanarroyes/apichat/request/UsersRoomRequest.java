package com.juanarroyes.apichat.request;

import java.util.List;

public class UsersRoomRequest {

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
