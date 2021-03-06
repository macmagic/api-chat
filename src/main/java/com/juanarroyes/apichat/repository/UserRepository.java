package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    User findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);
}
