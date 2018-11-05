package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import com.juanarroyes.apichat.model.UserProfileKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileKey> {

    Optional<UserProfile> findByUserId(UserProfileKey user);
}
