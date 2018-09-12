package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.UserRepository;
import com.juanarroyes.apichat.util.PasswordAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String email, String password) throws UserAlreadyExistException {

        User userFound = userRepository.findByEmail(email);

        if(userFound != null){
            throw new UserAlreadyExistException("User already exists with this email");
        }

        User user = new User();
        user.setEmail(email);

        PasswordAuth passwordAuth = new PasswordAuth();
        user.setPassword(passwordAuth.hash(password.toCharArray()));
        user.setStatus(1);
        return userRepository.save(user);
    }

    public User getUser(Long id) throws UserNotFoundException{

        User user = null;
        Optional<User> userFound = userRepository.findById(id);

        if(!userFound.isPresent()){
            throw new UserNotFoundException("User not found");
        }
        user = userFound.get();
        return user;
    }

    public List<User> getUsers(){
        List<User> usersFound = null;
        return userRepository.findAll();
    }
}
