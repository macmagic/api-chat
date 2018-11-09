package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    List<Message> findByChatId(Long chatId);

}
