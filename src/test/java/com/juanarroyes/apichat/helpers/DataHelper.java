package com.juanarroyes.apichat.helpers;

import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import com.juanarroyes.apichat.model.UserProfileKey;
import com.juanarroyes.apichat.model.UserRequest;

import java.util.Date;
import java.util.Random;

public class DataHelper {

    private static final String USER_EMAIL = "email{id}@example.com";
    private static final String USER_PASSWORD = "$31$16$BU_GMoncEDZzyGcB34AlC6LIKngxtcL4whKbRNpdFRA";
    private static final int USER_STATUS = 1;

    private static final String USER_PROFILE_FIRSTNAME = "Pepe";
    private static final String USER_PROFILE_LASTNAME = "Navarro";
    private static final String USER_PROFILE_NICKNAME = "Sparta";
    private static final String USER_PROFILE_COUNTRY = "Spain";
    private static final String USER_PROFILE_ADDRESS = "c/ Fake Street nº 123 3a-2n";
    private static final String USER_PROFILE_LOCATION = "Springfield";
    private static final String USER_PROFILE_PHONE = "555-44-33-22";

    private static final Date STATIC_NOW = new Date();

    public static User getRandomUser() {
        User user = new User();
        String emailId = String.valueOf(new Random().nextInt(1000));
        user.setEmail(USER_EMAIL.replace("{id}", emailId));
        user.setPassword(USER_PASSWORD);
        user.setStatus(USER_STATUS);
        user.setCreated(STATIC_NOW);
        return user;
    }

    public static UserRequest getUserRequest(User userOwner, User userDemand) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUser(userOwner);
        userRequest.setUserRequest(userDemand);
        userRequest.setCreated(STATIC_NOW);
        return userRequest;
    }

    public static UserProfile getUserProfile(User user) {
        UserProfile userProfile = new UserProfile();
        UserProfileKey userProfileKey = new UserProfileKey(user);
        userProfile.setUserId(userProfileKey);
        userProfile.setFirstname(USER_PROFILE_FIRSTNAME);
        userProfile.setLastname(USER_PROFILE_LASTNAME);
        userProfile.setNickname(USER_PROFILE_NICKNAME);
        userProfile.setAddress(USER_PROFILE_ADDRESS);
        userProfile.setLocation(USER_PROFILE_LOCATION);
        userProfile.setCountry(USER_PROFILE_COUNTRY);
        userProfile.setBirthday(STATIC_NOW);
        userProfile.setPhone(USER_PROFILE_PHONE);
        userProfile.setCreated(STATIC_NOW);
        return userProfile;
    }

}
