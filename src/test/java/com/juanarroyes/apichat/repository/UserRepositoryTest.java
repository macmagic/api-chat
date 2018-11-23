package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.ApiChatApplication;
import com.juanarroyes.apichat.helpers.DataHelper;
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
public class UserRepositoryTest {

    private static final int EXPECTED_LIST_COUNT = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

    private List<User> users;

    @Before
    public void init() {
        user = DataHelper.getRandomUser();
        entityManager.persist(user);
    }

    @After
    public void clean() {
        entityManager.clear();
    }

    @Test
    public void testFindAll() throws SQLException {
        entityManager.persist(DataHelper.getRandomUser());
        entityManager.persist(DataHelper.getRandomUser());
        entityManager.persist(DataHelper.getRandomUser());
        List<User> users = userRepository.findAll();
        assertEquals(EXPECTED_LIST_COUNT, users.size());
    }

    @Test
    public void testFindByEmail() {
        entityManager.merge(this.user);
        User result = userRepository.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testFindById() {
        Long userId = (Long) entityManager.persistAndGetId(DataHelper.getRandomUser());
        Optional<User> result = userRepository.findById(userId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testExistsByEmail() {
        entityManager.merge(user);
        System.out.println(user.getId());
        boolean result = userRepository.existsByEmail(user.getEmail());
        assertTrue(result);
    }
}
