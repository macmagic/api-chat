package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ContactListAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserRequestAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserRequestNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;

import java.util.List;

public interface UserRequestService {

    List<UserRequest> getAllRequestByUser(User user);

    UserRequest getRequest(Long requestId, User user);

    UserRequest createRequest(User userOwner, User userRequest) throws ContactListAlreadyExistsException, UserRequestAlreadyExistsException;

    UserRequest getRequest(Long requestId) throws UserRequestNotFoundException;

    ContactList answerRequest(String answer, Long requestId, User user) throws UserRequestNotFoundException;

    void deleteRequest(UserRequest request);

}
