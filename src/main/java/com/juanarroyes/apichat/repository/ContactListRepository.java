package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.ContactList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactListRepository extends JpaRepository<ContactList, Long>{

    ContactList findByOwnerId(Long ownerId);

}
