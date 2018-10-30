package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.dto.UserObj;
import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    private UserService userService;

    @Autowired
    public UserController(TokenService tokenService, UserService userService){
        super(tokenService, userService);
        this.userService = userService;
    }

    @GetMapping("/id/{user_id}")
    public ResponseEntity<User> getUser(@PathVariable("user_id") Long userId){

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        User user = null;

        try {
            user = userService.getUser(userId);
            httpStatus = HttpStatus.OK;
        } catch(UserNotFoundException ex){
            log.info("User not found: " + userId);
            httpStatus = HttpStatus.NOT_FOUND;
        } catch(Exception ex){
            log.error("Unexpected exception in method getUser: " + ex.getMessage());
        }
        return new ResponseEntity<>(user, httpStatus);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(UserObj userObj){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        User user = null;

        try{
            String email = userObj.getEmail();
            String password = userObj.getPassword();


            if(email == null || email.equals("")){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            if(password == null || password.equals("")){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            try {
                user = userService.createUser(email, password);
                httpStatus = HttpStatus.CREATED;
            } catch(UserAlreadyExistException ex){
                log.info(ex.getMessage());
                throw new HttpClientErrorException(HttpStatus.CONFLICT);
            }
        } catch(HttpClientErrorException ex){
            httpStatus = ex.getStatusCode();
        } catch(Exception ex){
            log.error("Unexpected exception in method createUser", ex);
        }
        return new ResponseEntity<>(user, httpStatus);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<User> usersFound = new ArrayList<>();
        try {
             usersFound = userService.getUsers();
            if(usersFound.isEmpty()){
                httpStatus = HttpStatus.NO_CONTENT;
            } else {
                httpStatus = HttpStatus.OK;
            }
        } catch(HttpClientErrorException ex){
            httpStatus = ex.getStatusCode();
        } catch (Exception ex){
            log.error("Unexpected exception in method getUsers", ex);
        }
        return new ResponseEntity<>(usersFound, httpStatus);
    }
}
