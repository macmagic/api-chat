package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.RoomNotFoundException;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;

import java.util.List;

public interface RoomService {

    Room createRoom(String roomName, String roomMessage);

    Room getRoomById(Long id) throws RoomNotFoundException;

    void deleteRoomInfo(Room room);
}
