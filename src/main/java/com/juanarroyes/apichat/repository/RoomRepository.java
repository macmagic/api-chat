package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT r.* FROM room r INNER JOIN chat c ON c.room_id = r.id INNER JOIN chat_participant cha ON cha.chat_id = c.id WHERE cha.user_id = :user_id", nativeQuery = true)
    List<Room> findAllRoomsByUser(@Param("user_id") Long userId);
}
