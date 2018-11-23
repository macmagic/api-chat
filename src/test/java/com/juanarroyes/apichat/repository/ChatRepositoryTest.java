package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.ChatParticipant;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class ChatRepositoryTest {

    private static final int EXPECTED_CHAT_ROOM_COUNT = 3;
    private static final int EXPECTED_USER_CHATS = 2;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Chat chatPrivate;

    private User user;

    private User userFriend1;

    @Before
    public void init() {
        user = DataHelper.getRandomUser();
        entityManager.persist(user);
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testFindByPrivateChatByUsers() {
        User userFriend = DataHelper.getRandomUser();
        entityManager.persist(userFriend);
        Chat chat = DataHelper.getChatPrivate();
        entityManager.persist(chat);
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chat, user, false);
        entityManager.persist(chatParticipant);
        chatParticipant = DataHelper.getChatParticipant(chat, userFriend, false);
        entityManager.persist(chatParticipant);
        List<Long> users = new ArrayList<>();
        users.add(user.getId());
        users.add(userFriend.getId());
        Optional<Chat> result = chatRepository.findByPrivateChatByUsers(users);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByChatAndUser() {
        Chat chat = DataHelper.getChatPrivate();
        entityManager.persist(chat);
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chat, user);
        entityManager.persist(chatParticipant);
        Optional<Chat> result = chatRepository.findByChatAndUser(chat.getId(), user.getId());
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindAllChatRoomsByUser() {
        Room room = DataHelper.getRandomRoom();
        entityManager.persist(room);
        Chat chat = DataHelper.getChatRoom(room);
        entityManager.persist(chat);
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chat, user, true);
        entityManager.persist(chatParticipant);
        room = DataHelper.getRandomRoom();
        entityManager.persist(room);
        chat = DataHelper.getChatRoom(room);
        entityManager.persist(chat);
        chatParticipant = DataHelper.getChatParticipant(chat, user, true);
        entityManager.persist(chatParticipant);
        room = DataHelper.getRandomRoom();
        entityManager.persist(room);
        chat = DataHelper.getChatRoom(room);
        entityManager.persist(chat);
        chatParticipant = DataHelper.getChatParticipant(chat, user, true);
        entityManager.persist(chatParticipant);
        List<Chat> result = chatRepository.findAllChatRoomsByUser(user.getId());
        assertEquals(EXPECTED_CHAT_ROOM_COUNT, result.size());
    }

    @Test
    public void testFindAllByUser() {
        Chat chat = DataHelper.getChatPrivate();
        entityManager.persist(chat);
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chat, user);
        entityManager.persist(chatParticipant);
        chat = DataHelper.getChatPrivate();
        entityManager.persist(chat);
        chatParticipant = DataHelper.getChatParticipant(chat, user);
        entityManager.persist(chatParticipant);
        List<Chat> result = chatRepository.findAllByUser(user.getId());
        assertEquals(EXPECTED_USER_CHATS, result.size());
    }
}
