package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Message;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService{

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
    @Override
    public List<Message> getMessagesByChat(Chat chat) {
        return messageRepository.findByChatId(chat.getId());
    }

    public Message sendMessage(User user, Chat chat, String messageText) {
        return sendMessage(user, chat, messageText, 1);
    }

    @Override
    public Message sendMessage(User user, Chat chat, String messageText, Integer messageType) {
        return sendMessage(user, chat, messageText, messageType, null);
    }

    @Override
    public Message sendMessage(User user, Chat chat, String messageText, Integer messageType, String attachUrl) {

        Message message = new Message();
        message.setAuthorId(user.getId());
        message.setMessageText(messageText);
        message.setChatId(chat.getId());
        message.setMessageType(messageType);

        if(attachUrl != null) {
            message.setAttachUrl(attachUrl);
        }
        return messageRepository.save(message);
    }
}
