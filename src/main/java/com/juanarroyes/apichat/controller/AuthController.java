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
import com.juanarroyes.apichat.service.TokenService;
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
import org.springframework.web.client.HttpClientErrorException;
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

    private AuthenticationManager authenticationManager;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private TokenService tokenService;

    private UserRepository userRepository;

    RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService,
                          UserRepository userRepository,
                          RefreshTokenRepository refreshTokenRepository){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody UserObj userObj){

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        JwtAuthResponse response = null;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userObj.getEmail(), userObj.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            String accessToken = tokenService .generateToken(userPrincipal);
            String refreshToken = tokenService.generateRefreshToken();

            try {
                tokenService.saveRefreshToken(userPrincipal, refreshToken);
            } catch (UserNotFoundException ex) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            response = new JwtAuthResponse(accessToken, refreshToken, jwtExpirationInMs);
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch (Exception ex) {
            log.error("Unexpected error in method authenticateUser", ex);
        }
        return new ResponseEntity<>(response, httpStatus);
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
            String accessToken = tokenService.generateToken(UserPrincipal.create(user));
            return ResponseEntity.ok(new JwtAuthResponse(accessToken, refreshToken.getToken(), jwtExpirationInMs));
        }).orElseThrow(() -> new BadRequestException("Invalid Refresh Token"));
    }

    private void saveRefreshToken(UserPrincipal userPrincipal, String tokenRefresh) {
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(tokenRefresh);
            refreshToken.setUser(userService.getUser(userPrincipal.getId()));
            Instant expirationTime = Instant.now().plus(30, ChronoUnit.DAYS);
            refreshToken.setExpirationTime(expirationTime);
            refreshTokenRepository.save(refreshToken);
        } catch (UserNotFoundException ex) {
            log.error("User not found", ex);
        }
    }
}
