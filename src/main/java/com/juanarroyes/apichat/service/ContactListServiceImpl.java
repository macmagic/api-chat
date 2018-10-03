package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.ContactListStatus;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.ContactListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ContactListServiceImpl {

    private ContactListRepository contactListRepository;

    @Autowired
    public ContactListServiceImpl(ContactListRepository contactListRepository) {
        this.contactListRepository = contactListRepository;
    }

    public boolean createRequest(User userOwner, User userRequest) {
        try {
            ContactList request = new ContactList();
            request.setOwnerId(userOwner.getId());
            request.setContactId(userRequest.getId());
            request.setStatus(ContactListStatus.CONTACT_IS_FRIEND);
            contactListRepository.save(request);

            request = new ContactList();
            request.setOwnerId(userRequest.getId());
            request.setContactId(userOwner.getId());
            request.setStatus(ContactListStatus.CONTACT_IS_PENDING);
            contactListRepository.save(request);
            return true;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRequest", ex);
        }
        return false;
    }

    public boolean answerRequest(User userOwner, String response) {

        if(response.equals(ContactList.USER_ALLOW)) {
            return allowRequest(userOwner);
        } else if (response.equals(ContactList.USER_DENY)) {

        }



        return false;
    }

   // @Transactional
    public boolean createRelationship(User userRequest, User userRequired) {
        try {
            ContactList result = null;

            ContactList contact = new ContactList();
            contact.setOwnerId(userRequest.getId());
            contact.setContactId(userRequired.getId());
            contact.setStatus(ContactListStatus.CONTACT_IS_FRIEND);
            result = contactListRepository.save(contact);

            contact = new ContactList();
            contact.setOwnerId(userRequired.getId());
            contact.setContactId(userRequest.getId());
            result = contactListRepository.save(contact);
            return true;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRelationship", ex);
        }
        return false;
    }

    private boolean allowRequest(User userOwner) {
        ContactList contactList = contactListRepository.findByOwnerId(userOwner.getId());
        contactList.setStatus(ContactListStatus.CONTACT_IS_FRIEND);
        contactListRepository.save(contactList);
        return true;
    }

    /*public boolean acceptRequest(User userOwner) throws ContactListNotFoundException{
        ContactList contactList = contactListRepository.findByOwnerId(userOwner.getId());

        if(contactList == null) {
            throw new ContactListNotFoundException("Contact not found");
        }


    }*/
}
