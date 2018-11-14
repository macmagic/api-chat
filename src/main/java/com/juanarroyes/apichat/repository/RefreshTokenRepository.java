package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
