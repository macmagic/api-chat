package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.User;

import java.util.List;

public interface UserService {

    User createUser(String email, String password) throws UserAlreadyExistException;

    User getUser(Long id) throws UserNotFoundException;

    List<User> getUsers();

    User getUserByEmail(String email) throws UserNotFoundException;

    boolean existsByUsername(String email);
}
