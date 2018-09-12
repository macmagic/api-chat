package com.juanarroyes.apichat.exception;

public class UserAlreadyExistException extends ApiChatException {

    public UserAlreadyExistException(){
    }

    public UserAlreadyExistException(String message){
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }

    public UserAlreadyExistException(Throwable cause){
        super(cause);
    }
}
