package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.RoomNotFoundException;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;

import java.util.List;

public interface RoomService {

    List<Room> getRoomsByUser(User user);

    Room createRoom(String room);

    Room getRoomById(Long id) throws RoomNotFoundException;
}
