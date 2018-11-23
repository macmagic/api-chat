package com.juanarroyes.apichat.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ChatParticipantRepositoryTest.class,
        ChatRepositoryTest.class,
        ContactListRepositoryTest.class,
        MessageRepositoryTest.class,
        RefreshTokenRepositoryTest.class,
        UserProfileRepositoryTest.class,
        UserRepositoryTest.class,
        UserRequestRepositoryTest.class})
public class RepositoryTestAll {
}
