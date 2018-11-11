package com.juanarroyes.apichat.response;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Room;

public class RoomCreationResponse {

    private Room room;

    private Chat chat;

    public RoomCreationResponse(Room room, Chat chat) {
        this.chat = chat;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
