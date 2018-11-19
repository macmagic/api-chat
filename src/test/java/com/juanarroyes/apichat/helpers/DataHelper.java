package com.juanarroyes.apichat.helpers;

import com.juanarroyes.apichat.model.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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

    private static final String ROOM_NAME = "Test room {id}";
    private static final String ROOM_MESSAGE_BROADCAST = "This is a room test!";

    private static final String REFRESH_TOKEN = "0e6ceebb-1304-47d5-b3cc-f69faf5bb44d";

    private static final String CHAT_SESSION_ID = "gPLgJBjF0cbMIPXQyOoi98fSLHuuOKctQeoZRy9m";

    private static final String MESSAGE_TEXT = "Hello number {id}";

    private static final int CONTACT_STATUS_FRIEND = 2;

    private static final Date STATIC_NOW = new Date();

    /**
     *
     * @return
     */
    public static User getRandomUser() {
        User user = new User();
        String emailId = String.valueOf(new Random().nextInt(1000));
        user.setEmail(USER_EMAIL.replace("{id}", emailId));
        user.setPassword(USER_PASSWORD);
        user.setStatus(USER_STATUS);
        user.setCreated(STATIC_NOW);
        return user;
    }

    /**
     *
     * @param userOwner
     * @param userDemand
     * @return
     */
    public static UserRequest getUserRequest(User userOwner, User userDemand) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUser(userOwner);
        userRequest.setUserRequest(userDemand);
        userRequest.setCreated(STATIC_NOW);
        return userRequest;
    }

    /**
     *
     * @param user
     * @return
     */
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

    /**
     *
     * @return
     */
    public static Room getRandomRoom() {
        Room room = new Room();
        room.setRoomName(ROOM_NAME);
        room.setRoomMessageBroadcast(ROOM_MESSAGE_BROADCAST);
        room.setCreated(STATIC_NOW);
        return room;
    }

    /**
     *
     * @param user
     * @return
     */
    public static RefreshToken getRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(REFRESH_TOKEN);
        refreshToken.setExpirationTime(Instant.now().plus(30, ChronoUnit.DAYS));
        return refreshToken;
    }

    public static ContactList getContactList(User userOwner, User userFriend) {
        ContactList contactList = new ContactList();
        contactList.setOwnerId(userOwner.getId());
        contactList.setContactId(userFriend.getId());
        contactList.setStatus(CONTACT_STATUS_FRIEND);
        contactList.setCreated(STATIC_NOW);
        return contactList;
    }

    /**
     *
     * @return
     */
    public static Chat getChatPrivate() {
        Chat chat = new Chat();
        chat.setPrivate(true);
        chat.setIsRoom(false);
        chat.setSessionId(CHAT_SESSION_ID);
        chat.setCreated(STATIC_NOW);
        return chat;
    }

    public static Chat getChatRoom(Room room) {
        Chat chat = new Chat();
        chat.setPrivate(false);
        chat.setIsRoom(true);
        chat.setRoom(room);
        chat.setSessionId(CHAT_SESSION_ID);
        chat.setCreated(STATIC_NOW);
        return chat;
    }

    public static ChatParticipant getChatParticipant(Chat chat, User user) {
        return getChatParticipant(chat, user, false);
    }

    /**
     *
     * @param chat
     * @param user
     * @param isAdmin
     * @return
     */
    public static ChatParticipant getChatParticipant(Chat chat, User user, boolean isAdmin) {
        ChatParticipantKey id = new ChatParticipantKey(chat.getId(), user.getId());
        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setId(id);
        chatParticipant.setAdmin(isAdmin);
        chatParticipant.setCreated(STATIC_NOW);
        return chatParticipant;
    }

    /**
     *
     * @param chat
     * @param user
     * @return
     */
    public static Message getRandomMessage(Chat chat, User user) {
        Message message = new Message();
        String random = String.valueOf(new Random().nextInt(1000));
        message.setMessageText(MESSAGE_TEXT.replace("{id}", random));
        message.setAuthorId(user.getId());
        message.setChatId(chat.getId());
        message.setCreated(STATIC_NOW);
        return message;
    }

    public static Long getRandomId() {
        Long randomId = new Random().nextLong();
        return randomId;
    }
}
