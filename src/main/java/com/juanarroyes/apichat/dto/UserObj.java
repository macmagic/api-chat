package com.juanarroyes.apichat.dto;

public class UserObj {
    private String email;
    private String password;

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public String toString(){
        return "Email is: " + email + " and password is: " + password;
    }

}
