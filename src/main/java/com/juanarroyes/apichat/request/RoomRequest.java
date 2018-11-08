package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class RoomRequest {

    @JsonAlias("room_name")
    private String roomName;

    @JsonAlias("room_message")
    private String roomMessage;

    @JsonAlias("users_room")
    private List<Long> usersRoom;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomMessage() {
        return roomMessage;
    }

    public void setRoomMessage(String roomMessage) {
        this.roomMessage = roomMessage;
    }

    public List<Long> getUsersRoom() {
        return usersRoom;
    }

    public void setUsersRoom(List<Long> usersRoom) {
        this.usersRoom = usersRoom;
    }
}
