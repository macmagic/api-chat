package com.juanarroyes.apichat.service;


import com.juanarroyes.apichat.exception.ContactListAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserRequestAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserRequestNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.model.UserRequest;
import com.juanarroyes.apichat.repository.UserRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserRequestService {

    private UserRequestRepository userRequestRepository;

    private ContactListService contactListService;

    @Autowired
    public UserRequestService(UserRequestRepository userRequestRepository, ContactListService contactListService) {
        this.userRequestRepository = userRequestRepository;
        this.contactListService = contactListService;
    }

    /**
     *
     * @param user
     * @return
     */
    public List<UserRequest> getAllRequestByUser(User user) {
        return userRequestRepository.findByUser(user);
    }

    /**
     *
     * @param userOwner
     * @param userRequest
     * @return
     * @throws ContactListAlreadyExistsException
     * @throws UserRequestAlreadyExistsException
     */
    @Transactional
    public UserRequest createRequest(User userOwner, User userRequest) throws ContactListAlreadyExistsException, UserRequestAlreadyExistsException {
        ContactList contactList = contactListService.getContactByOwnerUserAndFriend(userRequest, userOwner);

        if (contactList != null) {
            throw new ContactListAlreadyExistsException("Contact already exists");
        }

        contactListService.createRelation(userRequest, userOwner);

        UserRequest result = userRequestRepository.findByUserAndUserRequest(userOwner, userRequest);

        if(result != null) {
            throw new UserRequestAlreadyExistsException("Request already exists");
        }

        UserRequest request = new UserRequest();
        request.setUser(userOwner);
        request.setUserRequest(userRequest);
        return userRequestRepository.save(request);
    }

    /**
     *
     * @param requestId
     * @return
     * @throws UserRequestNotFoundException
     */
    public UserRequest getRequest(Long requestId) throws UserRequestNotFoundException {

        Optional<UserRequest> result = userRequestRepository.findById(requestId);

        if(!result.isPresent()) {
            throw new UserRequestNotFoundException("Request not found");
        }

        return result.get();
    }

    /**
     *
     * @param request
     */
    public void deleteRequest(UserRequest request) {
        userRequestRepository.delete(request);
    }
}
