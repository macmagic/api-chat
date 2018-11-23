package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.helpers.DataHelper;
import com.juanarroyes.apichat.model.RefreshToken;
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

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private RefreshToken refreshToken;

    private User user;

    @Before
    public void init() {
        user = DataHelper.getRandomUser();
        entityManager.persist(user);
        this.refreshToken = DataHelper.getRefreshToken(user);
    }

    @After
    public void clear() {
        entityManager.clear();
    }

    @Test
    public void testDeleteByUserId() {
        entityManager.persist(refreshToken);
        refreshTokenRepository.deleteByUserId(user.getId());
        Optional<RefreshToken> result = refreshTokenRepository.findById(refreshToken.getToken());
        assertFalse(result.isPresent());
    }

}
