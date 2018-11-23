package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class UserRequestRepositoryTest {
    private final static String TEST_USER_EMAIL = "email@example.com";
    private final static String TEST_USER_PASSWORD = "$31$16$BU_GMoncEDZzyGcB34AlC6LIKngxtcL4whKbRNpdFRA";
    private final static int TEST_USER_STATUS = 1;

    private final static String TEST_USER_REQUESTED_EMAIL = "email2@example.com";
    private final static String  TEST_USER_REQUESTED_PASSWORD = "$31$16$BU_GMoncEDZzyGcB34AlC6LIKngxtcL4whKbRNpdFRA";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRequestRepository userRequestRepository;

    private User user;

    private User userRequested;

    private UserRequest userRequest;

    @Before
    public void setUp() {
        setUsers();
        setUserRequest();
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testFindByUserAndUserRequest() {
        entityManager.persist(userRequest);
        UserRequest userRequest = userRequestRepository.findByUserAndUserRequest(userRequested, user);
        assertNotNull(userRequest);
    }

    @Test
    public void testFindByUser() {
        entityManager.merge(userRequest);
        List<UserRequest> requestList = userRequestRepository.findByUser(userRequested);
        assertEquals(1, requestList.size());
    }

    @Test
    public void testFindByRequestIdAndUser() {
        UserRequest result = userRequestRepository.findByRequestIdAndUser(userRequest.getRequestId(), userRequested);
        assertNotNull(result);
    }

    private void setUsers() {
        user = new User();
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(TEST_USER_PASSWORD);
        user.setStatus(TEST_USER_STATUS);
        entityManager.persist(user);

        userRequested = new User();
        userRequested.setEmail(TEST_USER_REQUESTED_EMAIL);
        userRequested.setPassword(TEST_USER_REQUESTED_PASSWORD);
        userRequested.setStatus(TEST_USER_STATUS);
        entityManager.persist(userRequested);
    }

    private void setUserRequest() {
        userRequest = new UserRequest();
        userRequest.setUser(userRequested);
        userRequest.setUserRequest(user);
        userRequest.setCreated(new Date());
        entityManager.persist(userRequest);
        userRequest.setRequestId(userRequest.getRequestId());
    }
}
