package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
