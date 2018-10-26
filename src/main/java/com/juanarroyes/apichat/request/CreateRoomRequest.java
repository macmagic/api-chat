package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class CreateRoomRequest {

    @JsonAlias("room_name")
    private String roomName;

    @JsonAlias("users_room")
    private List<Long> usersRoom;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Long> getUsersRoom() {
        return usersRoom;
    }

    public void setUsersRoom(List<Long> usersRoom) {
        this.usersRoom = usersRoom;
    }
}
