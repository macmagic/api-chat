package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>{

}
