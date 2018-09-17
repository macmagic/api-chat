package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.RoomNotFoundException;
import com.juanarroyes.apichat.model.Room;

public interface RoomService {

    Room createRoom(Room room);

    Room getRoomById(Long id) throws RoomNotFoundException;
}
