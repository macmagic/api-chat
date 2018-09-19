package com.juanarroyes.apichat.service;

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

    @Transactional
    public boolean createRelationship(User userRequest, User userRequired) {
        try {
            ContactList result = null;

            ContactList contact = new ContactList();
            contact.setUserId(userRequest.getId());
            contact.setContactUserId(userRequired.getId());
            contact.setStatus(ContactListStatus.CONTACT_IS_FRIEND);
            result = contactListRepository.save(contact);

            contact = new ContactList();
            contact.setUserId(userRequired.getId());
            contact.setContactUserId(userRequest.getId());
            result = contactListRepository.save(contact);
            return true;
        } catch(Exception ex) {
            log.error("Unexpected error in method createRelationship", ex);
        }
        return false;
    }
}
