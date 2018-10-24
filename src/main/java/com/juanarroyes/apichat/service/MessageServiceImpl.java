package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl {

    public MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     *
      * @param chat
     * @return
     *
     * @TODO Pagination messages we need on this place!
     */
    public List<Message> getMessagesByChat(Chat chat) {
        return messageRepository.findByChatId(chat.getId());
    }
}
