package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "SELECT c FROM chat c INNER JOIN chat_participant cp ON cp.chat_id = c.id WHERE c.is_private = 1 AND cp.user_id IN(:users)", nativeQuery = true)
    Optional<Chat> findByPrivateChatByUsers(@Param("users") List<Long> users);
}
