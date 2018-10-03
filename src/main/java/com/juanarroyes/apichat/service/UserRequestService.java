package com.juanarroyes.apichat.service;


import com.juanarroyes.apichat.repository.UserRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRequestService {

    private UserRequestRepository userRequestRepository;

    @Autowired
    public UserRequestService(UserRequestRepository userRequestRepository) {
        this.userRequestRepository = userRequestRepository;
    }
}
