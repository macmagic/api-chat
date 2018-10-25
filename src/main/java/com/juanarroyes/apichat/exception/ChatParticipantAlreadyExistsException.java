package com.juanarroyes.apichat.exception;

public class ChatParticipantAlreadyExistsException extends ApiChatException {

    public ChatParticipantAlreadyExistsException() {

    }

    public ChatParticipantAlreadyExistsException(String message) {
        super(message);
    }

    public ChatParticipantAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatParticipantAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
