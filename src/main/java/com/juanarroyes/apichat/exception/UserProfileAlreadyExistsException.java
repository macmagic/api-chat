package com.juanarroyes.apichat.exception;

public class UserProfileAlreadyExistsException extends ApiChatException {

    public UserProfileAlreadyExistsException() {

    }

    public UserProfileAlreadyExistsException(String message) {
        super(message);
    }

    public UserProfileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserProfileAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
