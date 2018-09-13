package com.juanarroyes.apichat.service;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.RefreshToken;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.RefreshTokenRepository;
import com.juanarroyes.apichat.security.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class TokenServiceImpl implements TokenService {

    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtExpirationInMs")
    private int expiredInMs;

    private RefreshTokenRepository refreshTokenRepository;

    private UserService userService;

    @Autowired
    public TokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserService userService){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public String generateToken(UserPrincipal userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+ expiredInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsopported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    @Override
    public Long getUserIdByToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    @Override
    public RefreshToken saveRefreshToken(UserPrincipal userPrincipal, String refreshToken) throws UserNotFoundException{
        User user = userService.getUser(userPrincipal.getId());
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setToken(refreshToken);
        refreshTokenObj.setUser(user);
        Instant expirationTime = Instant.now().plus(30, ChronoUnit.DAYS);
        refreshTokenObj.setExpirationTime(expirationTime);
        clearOldRefreshToken(userPrincipal.getId());
        return refreshTokenRepository.save(refreshTokenObj);
    }

    private void clearOldRefreshToken(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
