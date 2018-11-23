package com.juanarroyes.apichat.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthControllerTest.class,
        ChatControllerTest.class,
        ChatRoomControllerTest.class,
        ContactControllerTest.class,
        MessageControllerTest.class,
        RequestControllerTest.class,
        UserControllerTest.class
})
public class RestControllerTest {
}
