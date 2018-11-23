package com.juanarroyes.apichat.exception;

public class ChatUserIsTheSameException extends ApiChatException {
    public ChatUserIsTheSameException() { }

    public ChatUserIsTheSameException(String message) {
        super(message);
    }

    public ChatUserIsTheSameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatUserIsTheSameException(Throwable cause) {
        super(cause);
    }

}
