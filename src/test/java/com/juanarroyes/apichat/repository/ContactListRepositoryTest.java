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
        entityManager.persist(userOwner);

        userFriend1 = DataHelper.getRandomUser();
        entityManager.persist(userFriend1);

        userFriend2 = DataHelper.getRandomUser();
        entityManager.persist(userFriend2);
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testFindByIdAndOwnerId() {
        ContactList contactList = DataHelper.getContactList(userOwner, userFriend1);
        entityManager.persist(contactList);
        ContactList result = contactListRepository.findByIdAndOwnerId(contactList.getId(), userOwner.getId());
        assertNotNull(result);
    }

    @Test
    public void testFindByOwnerIdAndContactId() {
        ContactList contactList = DataHelper.getContactList(userOwner, userFriend2);
        entityManager.persist(contactList);
        ContactList result = contactListRepository.findByOwnerIdAndContactId(userOwner.getId(), contactList.getContactId());
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
