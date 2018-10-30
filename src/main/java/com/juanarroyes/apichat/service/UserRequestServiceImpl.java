package com.juanarroyes.apichat.service;


import com.juanarroyes.apichat.exception.ContactListAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserRequestAlreadyExistsException;
import com.juanarroyes.apichat.exception.UserRequestNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.ContactListStatus;
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
public class UserRequestServiceImpl implements UserRequestService {

    private UserRequestRepository userRequestRepository;

    private ContactListService contactListService;

    @Autowired
    public UserRequestServiceImpl(UserRequestRepository userRequestRepository, ContactListService contactListService) {
        this.userRequestRepository = userRequestRepository;
        this.contactListService = contactListService;
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public List<UserRequest> getAllRequestByUser(User user) {
        return userRequestRepository.findByUser(user);
    }

    /**
     *
     * @param requestId
     * @param user
     * @return
     */
    @Override
    public UserRequest getRequest(Long requestId, User user) {
        return userRequestRepository.findByRequestIdAndUser(requestId, user);
    }

    /**
     *
     * @param userOwner
     * @param userRequest
     * @return
     * @throws ContactListAlreadyExistsException
     * @throws UserRequestAlreadyExistsException
     */
    @Override
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
    @Override
    public UserRequest getRequest(Long requestId) throws UserRequestNotFoundException {

        Optional<UserRequest> result = userRequestRepository.findById(requestId);

        if(!result.isPresent()) {
            throw new UserRequestNotFoundException("Request not found");
        }

        return result.get();
    }

    @Override
    public ContactList answerRequest(String answer, Long requestId, User user) throws UserRequestNotFoundException{

        ContactList contactList =  null;
        UserRequest userRequest = userRequestRepository.findByRequestIdAndUser(requestId, user);

        if(userRequest == null) {
            throw new UserRequestNotFoundException("Cannot find this request for this user");
        }

        if(answer.equals(UserRequest.REQUEST_ALLOW)) {
            contactList = contactListService.createRelation(userRequest.getUser(), userRequest.getUserRequest());
        }

        userRequestRepository.delete(userRequest);
        return contactList;
    }

    /**
     *
     * @param request
     */
    @Override
    public void deleteRequest(UserRequest request) {
        userRequestRepository.delete(request);
    }
}
