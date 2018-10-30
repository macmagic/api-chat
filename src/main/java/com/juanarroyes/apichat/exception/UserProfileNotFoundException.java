package com.juanarroyes.apichat.exception;

public class UserProfileNotFoundException extends ApiChatException {

    public UserProfileNotFoundException() {

    }

    public UserProfileNotFoundException (String message) {
        super(message);
    }

    public UserProfileNotFoundException (String message, Throwable cause) {
        super(message, cause);
    }

    public UserProfileNotFoundException (Throwable cause) {
        super(cause);
    }
}
