package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ChatParticipantNotFoundException;
import com.juanarroyes.apichat.exception.RoomNotFoundException;
import com.juanarroyes.apichat.exception.UserNotAllowedException;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private ChatParticipantService chatParticipantService;

    private RoomRepository roomRepository;
    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, ChatParticipantService chatParticipantService) {
        this.roomRepository = roomRepository;
        this.chatParticipantService = chatParticipantService;
    }

    @Override
    public List<Room> getRoomsByUser(User user) {
        return roomRepository.findAllRoomsByUser(user.getId());
    }

    @Override
    public Room createRoom(String roomName) {
        Room room = new Room();
        room.setRoomName(roomName);
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long roomId) throws RoomNotFoundException{
        Optional<Room> result = roomRepository.findById(roomId);

        if(!result.isPresent()) {
            throw new RoomNotFoundException("Room not found");
        }
        return result.get();
    }

    public void deleteRoom(Room room) {
        roomRepository.delete(room);
    }
}
