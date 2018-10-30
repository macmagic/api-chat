package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String email, String passwordHash) throws UserAlreadyExistException {

        User userFound = userRepository.findByEmail(email);

        if(userFound != null){
            throw new UserAlreadyExistException("User already exists with this email");
        }

        User user = new User();
        user.setEmail(email);

        user.setPassword(passwordHash);
        user.setStatus(User.USER_ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id) throws UserNotFoundException {

        User user;
        Optional<User> userFound = userRepository.findById(id);

        if(!userFound.isPresent()){
            throw new UserNotFoundException("User not found");
        }
        user = userFound.get();
        return user;
    }

    @Override
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
