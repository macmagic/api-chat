package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.RefreshToken;
import com.juanarroyes.apichat.security.UserPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public interface TokenService {

    String generateToken(UserPrincipal userPrincipal);

    String generateRefreshToken();

    boolean validateToken(String token);

    Long getUserIdByToken(String token);

    RefreshToken saveRefreshToken(UserPrincipal userPrincipal, String tokenRefresh) throws UserNotFoundException;
}
