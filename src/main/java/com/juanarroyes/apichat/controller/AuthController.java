package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.dto.UserObj;
import com.juanarroyes.apichat.exception.BadRequestException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.RefreshToken;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.RefreshTokenRepository;
import com.juanarroyes.apichat.repository.UserRepository;
import com.juanarroyes.apichat.request.RefreshTokenRequest;
import com.juanarroyes.apichat.response.JwtAuthResponse;
import com.juanarroyes.apichat.security.JwtTokenProvider;
import com.juanarroyes.apichat.security.UserPrincipal;
import com.juanarroyes.apichat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sun.security.util.Password;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthenticationManager authenticationManager;

    UserService userService;

    PasswordEncoder passwordEncoder;

    JwtTokenProvider jwtTokenProvider;

    UserRepository userRepository;

    RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository,
                          RefreshTokenRepository refreshTokenRepository){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserObj userObj){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userObj.getEmail(), userObj.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwt = jwtTokenProvider .generateToken(userPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        saveRefreshToken(userPrincipal, refreshToken);
        return ResponseEntity.ok(new JwtAuthResponse(jwt, refreshToken, jwtExpirationInMs));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserObj userObj) {
        if(userService.existsByUsername(userObj.getEmail())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(userObj.getEmail());
        user.setPassword(passwordEncoder.encode(userObj.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body("User registered Correct");
    }

    @PostMapping("/token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenRepository.findById(refreshTokenRequest.getRefreshToken()).map(refreshToken -> {
            User user = refreshToken.getUser();
            String accessToken = jwtTokenProvider.generateToken(UserPrincipal.create(user));
            return ResponseEntity.ok(new JwtAuthResponse(accessToken, refreshToken.getToken(), jwtExpirationInMs));
        }).orElseThrow(() -> new BadRequestException("Invalid Refresh Token"));
    }

    private void saveRefreshToken(UserPrincipal userPrincipal, String tokenRefresh) {
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(tokenRefresh);
            refreshToken.setUser(userService.getUser(userPrincipal.getId()));
            Instant expirationTime = Instant.now().plus(360, ChronoUnit.DAYS);
            refreshToken.setExpirationTime(expirationTime);
            refreshTokenRepository.save(refreshToken);
        } catch (UserNotFoundException ex) {
            log.error("User not found", ex);
        }
    }
}
