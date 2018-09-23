package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ContactRequest {

    @JsonAlias("user_request_id")
    private Long userRequestId;

    @JsonAlias("user_required_id")
    private Long userRequiredId;

    public Long getUserRequestId() {
        return userRequestId;
    }

    public void setUserRequestId(Long userRequestId) {
        this.userRequestId = userRequestId;
    }

    public Long getUserRequiredId() {
        return userRequiredId;
    }

    public void setUserRequiredId(Long userRequiredId) {
        this.userRequiredId = userRequiredId;
    }
}
