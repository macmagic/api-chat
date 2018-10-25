package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

public class MessageRequest implements Serializable {

    @JsonAlias("chat_id")
    private Long chatId;

    @JsonAlias("message_text")
    private String messageText;

    @JsonAlias("message_type")
    private Integer messageType;

    @JsonAlias("attach_url")
    private String attachUrl;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }
}
