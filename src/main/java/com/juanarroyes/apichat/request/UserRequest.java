package com.juanarroyes.apichat.request;

public class UserRequest {

    private String email;

    private String password;

    private int status;

    public UserRequest () {

    }

    public UserRequest (String email, String password, int status) {
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
