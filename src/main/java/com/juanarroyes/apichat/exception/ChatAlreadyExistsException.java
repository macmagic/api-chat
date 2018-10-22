package com.juanarroyes.apichat.exception;

public class ChatAlreadyExistsException extends ApiChatException {

    public ChatAlreadyExistsException() {

    }

    public ChatAlreadyExistsException(String message) {
        super(message);
    }

    public ChatAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
