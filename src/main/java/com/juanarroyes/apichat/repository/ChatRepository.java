package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
