package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, User> {

}
