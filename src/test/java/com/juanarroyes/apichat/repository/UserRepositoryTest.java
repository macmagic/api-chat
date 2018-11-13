package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.ApiChatApplication;
import com.juanarroyes.apichat.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.activation.DataSource;
import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    private static final String TEST_USER_EMAIL = "email@example.com";
    private static final String TEST_USER_PASSWORD = "$31$16$BU_GMoncEDZzyGcB34AlC6LIKngxtcL4whKbRNpdFRA";
    private static final int TEST_USER_STATUS = 1;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationContext context;

    private User user;

    private List<User> users;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(TEST_USER_PASSWORD);
        user.setStatus(TEST_USER_STATUS);
        setUpListUsers();
    }

    @After
    public void clean() {
        entityManager.clear();
    }

    @Test
    public void testFindAll() throws SQLException {
        javax.sql.DataSource ds = (javax.sql.DataSource) context.getBean("dataSource");
        Connection c = ds.getConnection();

        entityManager.persist(generateRandomUsers());
        entityManager.persist(generateRandomUsers());
        entityManager.persist(generateRandomUsers());
        List<User> users = userRepository.findAll();
        assertEquals(3, users.size());
    }

    @Test
    public void testFindByEmail() {
        entityManager.merge(user);
        User user = userRepository.findByEmail(TEST_USER_EMAIL);
        assertEquals(TEST_USER_EMAIL, user.getEmail());
    }

    @Test
    public void testFindById() {
        entityManager.merge(user);
        Optional<User> result = userRepository.findById(1L);
        assertEquals(true, result.isPresent());
    }

    private User generateRandomUsers() {
        User user = new User();
        user.setEmail(String.valueOf(new Random().nextInt(200)) + TEST_USER_EMAIL);
        user.setPassword(TEST_USER_PASSWORD);
        user.setStatus(TEST_USER_STATUS);
        return user;
    }

    private void setUpListUsers() {
        users = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            User user = new User();
            user.setEmail(TEST_USER_EMAIL + String.valueOf(i));
            user.setPassword(TEST_USER_PASSWORD);
            user.setStatus(TEST_USER_STATUS);
            users.add(user);
        }
    }
}
