package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.ContactListStatus;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.ContactListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactListServiceImpl implements ContactListService{

    private ContactListRepository contactListRepository;

    @Autowired
    public ContactListServiceImpl(ContactListRepository contactListRepository) {
        this.contactListRepository = contactListRepository;
    }

    @Override
    public ContactList getContactListByIdAndOwnerId(Long contactId, Long userId) {
        return contactListRepository.findByIdAndOwnerId(contactId, userId);
    }

    @Override
    public List<ContactList> getContactsByUserId(Long userId) {
        return contactListRepository.findAllByOwnerId(userId);
    }

    /**
     *
     * @param userOwner
     * @param userFriend
     * @return ContactList Result of save data.
     */
    @Override
    public ContactList createRelation(User userOwner, User userFriend) {
        ContactList contactList = new ContactList();
        contactList.setOwnerId(userOwner.getId());
        contactList.setContactId(userFriend.getId());
        contactList.setStatus(ContactListStatus.CONTACT_IS_FRIEND);
        return contactListRepository.save(contactList);
    }

    @Override
    public ContactList getContactByOwnerUserAndFriend(User userOwner, User userFriend) {
        return contactListRepository.findByOwnerIdAndContactId(userOwner.getId(), userFriend.getId());
    }

    @Override
    public ContactList blockContact(Long contactId, Long userId) throws ContactListNotFoundException {
        ContactList contactList = contactListRepository.findByIdAndOwnerId(contactId, userId);
        if(contactList == null) {
            throw new ContactListNotFoundException("Contact not found");
        }
        contactList.setStatus(ContactListStatus.CONTACT_IS_BLOCKED);
        return contactListRepository.save(contactList);
    }

    @Override
    public ContactList unblockContact(Long contactId, Long userId) throws ContactListNotFoundException {
        ContactList contactList = contactListRepository.findByIdAndOwnerId(contactId, userId);
        if(contactList == null) {
            throw new ContactListNotFoundException("Contact not found");
        }
        contactList.setStatus(ContactListStatus.CONTACT_IS_FRIEND);
        return contactListRepository.save(contactList);
    }

    @Override
    public void deleteRelationById(Long contactId) throws ContactListNotFoundException {
        Optional<ContactList> result = contactListRepository.findById(contactId);
        if(!result.isPresent()) {
            throw new ContactListNotFoundException("Contact not found");
        }
        contactListRepository.deleteById(contactId);
    }

    @Override
    public void deleteRelation(User userOwner, User userFriend) throws ContactListNotFoundException{
        ContactList contactList = contactListRepository.findByOwnerIdAndContactId(userOwner.getId(), userFriend.getId());
        if(contactList == null) {
            throw new ContactListNotFoundException("Contact not found");
        }

        contactListRepository.deleteById(contactList.getId());
    }
}
