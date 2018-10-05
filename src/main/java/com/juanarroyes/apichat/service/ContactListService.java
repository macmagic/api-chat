package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.ContactListNotFoundException;
import com.juanarroyes.apichat.model.ContactList;
import com.juanarroyes.apichat.model.User;

public interface ContactListService {

    ContactList createRelation(User userOwner, User userFriend);

    ContactList getContactByOwnerUserAndFriend(User userOwner, User userFrined) throws ContactListNotFoundException;

    ContactList blockContact();

    void deleteRelationById(Long contactId) throws ContactListNotFoundException;

    void deleteRelation(User userOwner, User userFriend) throws ContactListNotFoundException;


}
