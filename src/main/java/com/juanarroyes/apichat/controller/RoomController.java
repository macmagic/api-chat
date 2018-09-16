package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.RoomNotFoundException;
import com.juanarroyes.apichat.model.Room;
import com.juanarroyes.apichat.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {

    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping()
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Room result = null;
        try {
            result = roomService.createRoom(room);
            httpStatus = HttpStatus.CREATED;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRoom", ex);
        }
        return new ResponseEntity<>(result, httpStatus);
    }


    @GetMapping("/id/{room_id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("room_id") Long id) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Room room = null;

        try {
            try {
                room = roomService.getRoomById(id);
            } catch(RoomNotFoundException ex) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
            httpStatus = HttpStatus.OK;
        } catch(HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch(Exception ex) {
            log.error("Unexpected error in method getRoomById", ex);
        }

        return new ResponseEntity<>(room, httpStatus);
    }
}
