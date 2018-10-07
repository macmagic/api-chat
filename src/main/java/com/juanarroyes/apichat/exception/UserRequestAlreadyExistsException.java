package com.juanarroyes.apichat.exception;

public class UserRequestAlreadyExistsException extends ApiChatException {

    public UserRequestAlreadyExistsException() {

    }

    public UserRequestAlreadyExistsException(String message) {
        super(message);
    }

    public UserRequestAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRequestAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
