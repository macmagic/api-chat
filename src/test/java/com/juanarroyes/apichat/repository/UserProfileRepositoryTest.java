package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import com.juanarroyes.apichat.model.UserProfileKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

    private UserProfile userProfile;

    @Before
    public void init() {
        user = DataHelper.getRandomUser();
        entityManager.persist(user);
        userProfile = DataHelper.getUserProfile(user);
    }

    @After
    public void clean() {
        entityManager.clear();
    }

    @Test
    public void testFindByUserId() {
        entityManager.persist(userProfile);
        UserProfileKey id = new UserProfileKey(user);
        Optional<UserProfile> result = userProfileRepository.findByUserId(id);
        assertTrue(result.isPresent());
    }
}
