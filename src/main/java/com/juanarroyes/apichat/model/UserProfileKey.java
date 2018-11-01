package com.juanarroyes.apichat.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
public class UserProfileKey implements Serializable {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    public UserProfileKey() {

    }

    public UserProfileKey(User user) {
        this.userId = user;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
