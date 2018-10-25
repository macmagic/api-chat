package com.juanarroyes.apichat.exception;

public class ChatParticipantNotFoundException extends ApiChatException {

    public ChatParticipantNotFoundException() {
    }

    public ChatParticipantNotFoundException(String message) {
        super(message);
    }

    public ChatParticipantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatParticipantNotFoundException(Throwable cause) {
        super(cause);
    }

}
