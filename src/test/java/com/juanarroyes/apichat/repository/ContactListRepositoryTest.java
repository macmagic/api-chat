package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.ContactList;
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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class ContactListRepositoryTest {

    private static final int EXPECTED_CONTACT_LIST_COUNT = 2;

    @Autowired
    private ContactListRepository contactListRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User userOwner;

    private User userFriend1;

    private User userFriend2;

    @Before
    public void init() {
        userOwner = DataHelper.getRandomUser();
        Long userId = (Long) entityManager.persistAndGetId(userOwner);
        userOwner.setId(userId);

        userFriend1 = DataHelper.getRandomUser();
        userId = (Long) entityManager.persistAndGetId(userFriend1);
        userFriend1.setId(userId);

        userFriend2 = DataHelper.getRandomUser();
        userId = (Long) entityManager.persistAndGetId(userFriend2);
        userFriend2.setId(userId);
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testFindByIdAndOwnerId() {
        ContactList contactList = DataHelper.getContactList(userOwner, userFriend1);
        Long contactId = (Long) entityManager.persistAndGetId(contactList);
        ContactList result = contactListRepository.findByIdAndOwnerId(contactId, userOwner.getId());
        assertNotNull(result);
    }

    @Test
    public void testFindByOwnerIdAndContactId() {
        ContactList contactList = DataHelper.getContactList(userOwner, userFriend2);
        Long contactId = (Long) entityManager.persistAndGetId(contactList);
        ContactList result = contactListRepository.findByOwnerIdAndContactId(userOwner.getId(), contactId);
        assertNotNull(result);
    }

    @Test
    public void testFindAllByOwnerId() {
        ContactList contactList = DataHelper.getContactList(userOwner, userFriend1);
        entityManager.persist(contactList);
        contactList = DataHelper.getContactList(userOwner, userFriend2);
        entityManager.persist(contactList);
        List<ContactList> result = contactListRepository.findAllByOwnerId(userOwner.getId());
        assertEquals(EXPECTED_CONTACT_LIST_COUNT, result.size());
    }
}
