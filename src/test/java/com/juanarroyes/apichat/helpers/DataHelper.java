package com.juanarroyes.apichat.helpers;

import com.juanarroyes.apichat.model.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DataHelper {


    //protected static final String USER_EMAIL = "testuser@example.com";
    //protected static final String USER_PASSWORD_RAW = "1234password";
    //protected static final String USER_PASSWORD = "$31$16$GmHTpJldiXpj5PpjCYSFuGAmde2DShDQoepigiENokA";

    // User data
    private static final Long USER_ID = 10000L;
    private static final String USER_EMAIL = "testuser@example.com";
    private static final String USER_EMAIL_TEMPLATE = "email{id}@example.com";
    private static final String USER_PASSWORD = "1234password";
    private static final String USER_PASSWORD_HASHED = "$31$16$GmHTpJldiXpj5PpjCYSFuGAmde2DShDQoepigiENokA";
    private static final int USER_STATUS = 1;

    //User profile data
    private static final String USER_PROFILE_FIRSTNAME = "Pepe";
    private static final String USER_PROFILE_LASTNAME = "Navarro";
    private static final String USER_PROFILE_NICKNAME = "Sparta";
    private static final String USER_PROFILE_COUNTRY = "Spain";
    private static final String USER_PROFILE_ADDRESS = "c/ Fake Street nº 123 3a-2n";
    private static final String USER_PROFILE_LOCATION = "Springfield";
    private static final String USER_PROFILE_PHONE = "555-44-33-22";

    private static final String ROOM_NAME = "Test room {id}";
    private static final String ROOM_MESSAGE_BROADCAST = "This is a room test!";

    //Token
    protected static final String USER_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTQyNjE4NDg0LCJleHAiOjE1NDI3MDQ4ODR9.OsywpD604kFhy-qRiJkw5mMw-94DY7Qn3WNmm93TOVcBYhP7R5gzmI_jdvUHJ_x3f1K693tUCjhXOshrBZiIgQ";
    protected static final Long USER_TOKEN_EXPIRATION = 86400000L;
    protected static final String USER_REFRESH_TOKEN = "f76224c9-643c-47ba-a416-d6cb01305960";

    private static final String REFRESH_TOKEN = "0e6ceebb-1304-47d5-b3cc-f69faf5bb44d";

    private static final String CHAT_SESSION_ID = "gPLgJBjF0cbMIPXQyOoi98fSLHuuOKctQeoZRy9m";

    private static final String MESSAGE_TEXT = "Hello number {id}";

    private static final int CONTACT_STATUS_FRIEND = 2;

    private static final int LIST_STATIC_COUNT = 10;

    private static final Date STATIC_NOW = new Date();

    /**
     *
     * @return
     */
    public static User getStaticUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD_HASHED);
        user.setStatus(USER_STATUS);
        user.setCreated(STATIC_NOW);
        return user;
    }

    public static String getStaticUserEmail() {
        return USER_EMAIL;
    }

    public static String getStaticPassword() {
        return USER_PASSWORD;
    }

    public static User getRandomUser() {
        return getRandomUser(null);
    }

    /**
     *
     * @return
     */
    public static User getRandomUser(Long userId) {
        User user = new User();
        user.setId(userId);
        String emailId = String.valueOf(new Random().nextInt(1000));
        user.setEmail(USER_EMAIL_TEMPLATE.replace("{id}", emailId));
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
        return getUserRequest(userOwner, userDemand, null);
    }

    /**
     *
     * @param userOwner
     * @param userDemand
     * @param userRequestId
     * @return
     */
    public static UserRequest getUserRequest(User userOwner, User userDemand, Long userRequestId) {
        UserRequest userRequest = new UserRequest();
        userRequest.setRequestId(userRequestId);
        userRequest.setUser(userOwner);
        userRequest.setUserRequest(userDemand);
        userRequest.setCreated(STATIC_NOW);
        return userRequest;
    }

    public static List<UserRequest> getListOfUserRequest() {
        List<UserRequest> userRequestList = new ArrayList<>();

        User userOwner = getRandomUser(new Random().nextLong());
        for(int i = 0; i < LIST_STATIC_COUNT; i++) {
            userRequestList.add(getUserRequest(userOwner, getRandomUser(new Random().nextLong())));
        }
        return userRequestList;
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

    public static Room getRandomRoom() {
        return getRandomRoom(null);
    }

    /**
     *
     * @return
     */
    public static Room getRandomRoom(Long roomId) {
        Room room = new Room();
        room.setId(roomId);
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
        return getContactList(userOwner, userFriend, null);
    }

    public static ContactList getContactList(User userOwner, User userFriend, Long contactListId) {
        ContactList contactList = new ContactList();
        contactList.setId(contactListId);
        contactList.setOwnerId(userOwner.getId());
        contactList.setContactId(userFriend.getId());
        contactList.setStatus(CONTACT_STATUS_FRIEND);
        contactList.setCreated(STATIC_NOW);
        return contactList;
    }

    public static List<ContactList> getListOfContacts() {
        List<ContactList> contacts = new ArrayList<>();
        User user = getRandomUser(1000L);
        for(int i = 0; i < LIST_STATIC_COUNT; i++) {
            contacts.add(getContactList(user, getRandomUser(new Random().nextLong())));
        }
        return contacts;
    }

    public static Chat getChatPrivate() {
        return getChatPrivate(null);
    }

    /**
     *
     * @return
     */
    public static Chat getChatPrivate(Long chatId) {
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setPrivate(true);
        chat.setIsRoom(false);
        chat.setSessionId(CHAT_SESSION_ID);
        chat.setCreated(STATIC_NOW);
        return chat;
    }

    public static Chat getChatRoom(Room room) {
        return getChatRoom(room, null);
    }

    public static Chat getChatRoom(Room room, Long chatId) {
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setPrivate(false);
        chat.setIsRoom(true);
        chat.setRoom(room);
        chat.setSessionId(CHAT_SESSION_ID);
        chat.setCreated(STATIC_NOW);
        return chat;
    }

    public static List<Chat> getListOfChatRooms() {
        List<Chat> chatRoomList = new ArrayList<>();

        for(int i = 0; i < LIST_STATIC_COUNT; i++) {
            Room room = getRandomRoom(new Random().nextLong());
            Chat chat = getChatRoom(room, new Random().nextLong());
            chatRoomList.add(chat);
        }

        return chatRoomList;
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

    public static List<ChatParticipant> getListOfChatParticipant() {
        Chat chat = getChatRoom(getRandomRoom(new Random().nextLong()));
        List<ChatParticipant> chatParticipantList = new ArrayList<>();

        for(int i = 0; i < LIST_STATIC_COUNT; i++) {
            chatParticipantList.add(DataHelper.getChatParticipant(chat, DataHelper.getRandomUser(new Random().nextLong())));
        }
        return chatParticipantList;
    }

    public static Message getRandomMessage(Chat chat, User user) {
        return getRandomMessage(chat, user, null);
    }

    /**
     *
     * @param chat
     * @param user
     * @return
     */
    public static Message getRandomMessage(Chat chat, User user, Long userId) {
        Message message = new Message();
        String random = String.valueOf(new Random().nextInt(1000));
        message.setMessageText(MESSAGE_TEXT.replace("{id}", random));
        message.setAuthorId(user.getId());
        message.setChatId(chat.getId());
        message.setCreated(STATIC_NOW);
        return message;
    }

    public static List<Message> getListOfRandomMessage(Chat chat) {
        List<Message> messageList = new ArrayList<>();

        for(int i = 0; i < LIST_STATIC_COUNT; i++) {
            messageList.add(getRandomMessage(chat, getRandomUser(new Random().nextLong())));
        }
        return messageList;
    }

    public static Long getRandomId() {
        Long randomId = new Random().nextLong();
        return randomId;
    }

    public static Date getStaticNow() {
        return STATIC_NOW;
    }
}
