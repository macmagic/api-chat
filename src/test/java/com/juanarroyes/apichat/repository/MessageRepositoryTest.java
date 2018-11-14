package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.Chat;
import com.juanarroyes.apichat.model.Message;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class MessageRepositoryTest {

    private static final int EXPECTED_MESSAGES_COUNT = 3;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Message message;

    private User user;

    private Chat chat;

    @Before
    public void init() {
        user = DataHelper.getRandomUser();
        Long userId = (Long) entityManager.persistAndGetId(user);
        user.setId(userId);

        chat = DataHelper.getPrivateChat();
        Long chatId = (Long) entityManager.persistAndGetId(chat);
        chat.setId(chatId);
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testFindByChatId() {
        message = DataHelper.getRandomMessage(chat, user);
        entityManager.persist(message);
        message = DataHelper.getRandomMessage(chat, user);
        entityManager.persist(message);
        message = DataHelper.getRandomMessage(chat, user);
        entityManager.persist(message);

        List<Message> result = messageRepository.findByChatId(chat.getId());
        assertEquals(EXPECTED_MESSAGES_COUNT, result.size());
    }
}
