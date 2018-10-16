package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {

    UserRequest findByUserAndUserRequest(User user, User userRequest);

    List<UserRequest> findByUser(User user);

    UserRequest findByRequestIdAndUser(Long requestId, User user);
}
