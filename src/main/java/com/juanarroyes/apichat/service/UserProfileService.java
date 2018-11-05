package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserProfileAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserProfileNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import com.juanarroyes.apichat.request.UserProfileRequest;

public interface UserProfileService {

    UserProfile getProfileByUser(User user) throws UserProfileNotFoundException;

    UserProfile createProfile(UserProfile profile, User user) throws UserProfileAlreadyExistsException;

    UserProfile createProfile(UserProfileRequest request, User user) throws UserProfileAlreadyExistsException;

    UserProfile updateProfile(UserProfileRequest request, User user) throws UserProfileNotFoundException;
}
