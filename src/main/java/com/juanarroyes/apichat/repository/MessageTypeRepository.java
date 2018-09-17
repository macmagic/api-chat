package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTypeRepository extends JpaRepository<MessageType, Integer> {
}
