package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.ContactList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactListRepository extends JpaRepository<ContactList, Long>{

    ContactList findByOwnerId(Long ownerId);

    ContactList findByIdAndOwnerId(Long id, Long ownerId);

    ContactList findByOwnerIdAndContactId(Long ownerId, Long contactId);

    List<ContactList> findAllByOwnerId(Long userId);

}
