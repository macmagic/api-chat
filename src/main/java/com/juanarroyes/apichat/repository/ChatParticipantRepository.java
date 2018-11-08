package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.ChatParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantKey> {

    @Query(value = "SELECT cp.* FROM chat_participant cp WHERE cp.chat_id = :chat_id AND is_admin = :is_admin", nativeQuery = true)
    List<ChatParticipant> findAllByChatAndAdmin(@Param("chat_id") Long chatId, @Param("is_admin") Boolean isAdmin);

    @Query(value = "SELECT cp.* FROM chat_participant cp WHERE cp.chat_id = :chat_id", nativeQuery = true)
    List<ChatParticipant> findAllByChat(@Param("chat_id") Long chatId);
}
