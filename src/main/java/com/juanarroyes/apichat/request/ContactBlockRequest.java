package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ContactBlockRequest {

    @JsonAlias(value = "contact_id")
    private Long contactId;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
