package com.juanarroyes.apichat.exception;

public class ContactListAlreadyExistsException extends ApiChatException {

    public ContactListAlreadyExistsException() {
    }

    public ContactListAlreadyExistsException(String message) {
        super(message);
    }

    public ContactListAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactListAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
