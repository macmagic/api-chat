package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.ChatParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantKey> {

    ChatParticipant findByChatIdAndUserId(Long chatId, Long userId);

}
