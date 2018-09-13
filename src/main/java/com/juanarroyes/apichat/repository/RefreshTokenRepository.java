package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    boolean deleteByUserId(Long userId);

}
