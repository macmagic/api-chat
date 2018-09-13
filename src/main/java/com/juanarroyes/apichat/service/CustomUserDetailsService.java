package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;

        try {
            user = userService.getUserByEmail(username);
        } catch(UserNotFoundException ex){
            throw new UsernameNotFoundException("Username not found");
        }

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long userId) {
        User user = null;
        try {
            user = userService.getUser(userId);
        } catch (UserNotFoundException ex) {
            log.error("User not found");
        }

        return UserPrincipal.create(user);
    }
}
