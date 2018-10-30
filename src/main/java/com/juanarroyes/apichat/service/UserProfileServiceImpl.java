package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserProfileNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import com.juanarroyes.apichat.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile getProfileByUser(User user) throws UserProfileNotFoundException {
        Optional<UserProfile> result = userProfileRepository.findById(user);
        if(!result.isPresent()) {
            throw new UserProfileNotFoundException("Cannot find user profile for user: " + user.getId());
        }

        return result.get();
    }
}
