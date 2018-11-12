package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private static final String TEST_USER_EMAIL = "email@example.com";
    private static final String TEST_USER_PASSWORD = "$31$16$BU_GMoncEDZzyGcB34AlC6LIKngxtcL4whKbRNpdFRA";
    private static final int TEST_USER_STATUS = 1;

    private UserRepository userRepository;

    private User user;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Before
    public void setUp() {
        user = new User();
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(TEST_USER_PASSWORD);
        user.setStatus(TEST_USER_STATUS);
    }
}
