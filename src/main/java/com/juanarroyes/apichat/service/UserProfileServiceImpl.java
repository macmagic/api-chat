package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserAlreadyExistException;
import com.juanarroyes.apichat.exception.UserProfileAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserProfileNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import com.juanarroyes.apichat.model.UserProfileKey;
import com.juanarroyes.apichat.repository.UserProfileRepository;
import com.juanarroyes.apichat.request.UserProfileRequest;
import com.juanarroyes.apichat.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
        UserProfileKey id = new UserProfileKey(user);
        Optional<UserProfile> result = userProfileRepository.findByUserId(id);
        if(!result.isPresent()) {
            throw new UserProfileNotFoundException("Cannot find user profile for user: " + user.getId());
        }

        return result.get();
    }

    public UserProfile createProfile(UserProfile profile, User user) throws UserProfileAlreadyExistsException {
        Optional<UserProfile> currentProfile = userProfileRepository.findByUserId(new UserProfileKey(user));

        if(currentProfile.isPresent()) {
            throw new UserProfileAlreadyExistsException("User " + user.getId() + " actually has a profile associate");
        }

        return userProfileRepository.save(profile);
    }

    /**
     *
     * @param request
     * @param user
     * @return
     * @throws UserProfileAlreadyExistsException
     */
    public UserProfile createProfile(UserProfileRequest request, User user) throws UserProfileAlreadyExistsException {
        Optional<UserProfile> currentProfile = userProfileRepository.findByUserId(new UserProfileKey(user));

        if(currentProfile.isPresent()) {
            throw new UserProfileAlreadyExistsException("User " + user.getId() + " actually has a profile associate");
        }
        return saveProfile(request, user);
    }

    public UserProfile updateProfile(UserProfileRequest request, User user) throws UserProfileNotFoundException {
        Optional<UserProfile> currentProfile = userProfileRepository.findByUserId(new UserProfileKey(user));

        if(!currentProfile.isPresent()) {
            throw new UserProfileNotFoundException("Cannot find user profile for this user");
        }

        return saveProfile(request, user);
    }

    private UserProfile saveProfile(UserProfileRequest data, User user) {
        UserProfile profile = new UserProfile();
        profile.setUserId(new UserProfileKey(user));
        profile.setFirstname(data.getFirstname());
        profile.setLastname(data.getLastname());
        profile.setNickname(data.getNickname());
        profile.setBirthday(Utils.getDateFromString(data.getBirthday(), "yyyy-M-d"));
        profile.setCountry(data.getCountry());
        profile.setLocation(data.getLocation());
        profile.setAddress(data.getAddress());
        profile.setPhone(data.getPhone());
        return userProfileRepository.save(profile);
    }
}
