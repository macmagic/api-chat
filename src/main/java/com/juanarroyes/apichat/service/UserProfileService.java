package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserProfileNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;

public interface UserProfileService {

    UserProfile getProfileByUser(User user) throws UserProfileNotFoundException;

}
