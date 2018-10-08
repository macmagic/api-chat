package com.juanarroyes.apichat.exception;

public class UserRequestNotFoundException extends ApiChatException {

    public UserRequestNotFoundException() {

    }

    public UserRequestNotFoundException(String message) {
        super(message);
    }

    public UserRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRequestNotFoundException(Throwable cause) {
        super(cause);
    }


}
