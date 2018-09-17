package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.RoomUser;
import com.juanarroyes.apichat.model.RoomUserKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, RoomUserKey> {
}
