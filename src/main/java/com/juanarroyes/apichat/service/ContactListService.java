package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;

import java.util.List;

public interface ContactListService {


    ContactList getContactListByIdAndOwnerId(Long contactId, Long userId);

    List<ContactList> getContactsByUserId(Long userId);

    ContactList createRelation(User userOwner, User userFriend);

    ContactList getContactByOwnerUserAndFriend(User userOwner, User userFriend);

    ContactList blockContact(Long contactId, Long userId) throws ContactListNotFoundException;

    ContactList unblockContact(Long contactId, Long userId) throws ContactListNotFoundException;

    void deleteRelationById(Long contactId) throws ContactListNotFoundException;

    void deleteRelation(User userOwner, User userFriend) throws ContactListNotFoundException;


}
