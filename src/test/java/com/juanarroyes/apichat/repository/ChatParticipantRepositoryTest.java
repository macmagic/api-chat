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

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class ChatParticipantRepositoryTest {

    private static final int EXPECTED_ADMIN_COUNT = 2;
    private static final int EXPECTED_PARTICIPANT_COUNT = 3;

    @Autowired
    private ChatParticipantRepository chatParticipantRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;

    private User user2;

    private User user3;

    private Chat chatRoom;

    @Before
    public void init() {
        user1 = DataHelper.getRandomUser();
        entityManager.persist(user1);

        user2 = DataHelper.getRandomUser();
        entityManager.persist(user2);

        user3 = DataHelper.getRandomUser();
        entityManager.persist(user3);

        Room room = DataHelper.getRandomRoom();
        entityManager.persist(room);

        chatRoom = DataHelper.getChatRoom(room);
        entityManager.persist(chatRoom);
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testFindAllByChatAndAdmin() {
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chatRoom, user1, true);
        entityManager.persist(chatParticipant);
        chatParticipant = DataHelper.getChatParticipant(chatRoom, user2, true);
        entityManager.persist(chatParticipant);
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByChatAndAdmin(chatRoom.getId(), true);
        assertEquals(EXPECTED_ADMIN_COUNT, chatParticipantList.size());
    }

    @Test
    public void testFindAllByChat() {
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chatRoom, user1, true);
        entityManager.persist(chatParticipant);
        chatParticipant = DataHelper.getChatParticipant(chatRoom, user2, true);
        entityManager.persist(chatParticipant);
        chatParticipant = DataHelper.getChatParticipant(chatRoom, user3, false);
        entityManager.persist(chatParticipant);
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByChat(chatRoom.getId());
        assertEquals(EXPECTED_PARTICIPANT_COUNT, chatParticipantList.size());
    }

    @Test
    public void testDeleteAllByChat() {
        ChatParticipant chatParticipant = DataHelper.getChatParticipant(chatRoom, user1, true);
        entityManager.persist(chatParticipant);
        chatParticipant = DataHelper.getChatParticipant(chatRoom, user2, true);
        entityManager.persist(chatParticipant);
        chatParticipant = DataHelper.getChatParticipant(chatRoom, user3, false);
        entityManager.persist(chatParticipant);
        chatParticipantRepository.deleteAllByChat(chatRoom.getId());
        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByChat(chatRoom.getId());
        assertTrue(chatParticipantList.isEmpty());
    }
}
