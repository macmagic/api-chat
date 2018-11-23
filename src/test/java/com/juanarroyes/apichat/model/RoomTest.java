package com.juanarroyes.apichat.model;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RoomTest {

    private static final String TEST_ROOM_NAME = "room_testing_01";
    private static final String TEST_ROOM_BROADCAST = "room testing broadcast";

    @Test
    public void testSetId() throws NoSuchFieldException, IllegalAccessException {
        final Room room = new Room();
        room.setRoomName(TEST_ROOM_NAME);
        room.setRoomMessageBroadcast(TEST_ROOM_BROADCAST);
        room.setId(1L);
        final Field field = room.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(room, 1L);
        assertEquals((Object) 1L, room.getId());
    }

    @Test
    public void testGetId() throws NoSuchFieldException, IllegalAccessException {
        final Room room = new Room();
        room.setRoomName(TEST_ROOM_NAME);
        room.setRoomMessageBroadcast(TEST_ROOM_BROADCAST);
        final Field field = room.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(room, 1L);
        assertEquals((Object) 1L, room.getId());
    }

    @Test
    public void testSetRoomName() throws NoSuchFieldException, IllegalAccessException {
        final Room room = new Room();
        room.setRoomName(TEST_ROOM_NAME);
        final Field field = room.getClass().getDeclaredField("roomName");
        field.setAccessible(true);
        assertEquals(TEST_ROOM_NAME, field.get(room));
    }

    @Test
    public void testGetRoomName() {
        final Room room = new Room(TEST_ROOM_NAME, TEST_ROOM_BROADCAST);
        assertEquals(TEST_ROOM_NAME, room.getRoomName());
    }

    @Test
    public void testSetRoomMessageBroadcast() throws NoSuchFieldException, IllegalAccessException {
        final Room room = new Room();
        room.setRoomMessageBroadcast(TEST_ROOM_BROADCAST);
        final Field field = room.getClass().getDeclaredField("roomMessageBroadcast");
        field.setAccessible(true);
        assertEquals(TEST_ROOM_BROADCAST, field.get(room));
    }

    @Test
    public void testGetRoomMessageBroadcast() {
        final Room room = new Room(TEST_ROOM_NAME, TEST_ROOM_BROADCAST);
        assertEquals(TEST_ROOM_BROADCAST, room.getRoomMessageBroadcast());
    }

    @Test
    public void testSetCreated() throws NoSuchFieldException, IllegalAccessException {
        final Room room = new Room(TEST_ROOM_NAME, TEST_ROOM_BROADCAST);
        Date now = new Date();
        room.setCreated(now);
        final Field field = room.getClass().getDeclaredField("created");
        field.setAccessible(true);
        assertEquals(now, field.get(room));
    }

    @Test
    public void testGetCreated() {
        final Room room = new Room(TEST_ROOM_NAME, TEST_ROOM_BROADCAST);
        Date now = new Date();
        room.setCreated(now);
        assertEquals(now, room.getCreated());
    }
}
