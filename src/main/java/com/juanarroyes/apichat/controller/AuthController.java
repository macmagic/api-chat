package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.dto.UserObj;
import com.juanarroyes.apichat.exception.BadRequestException;
import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.RefreshToken;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.RefreshTokenRepository;
import com.juanarroyes.apichat.repository.UserRepository;
import com.juanarroyes.apichat.request.RefreshTokenRequest;
import com.juanarroyes.apichat.response.AuthResponse;
import com.juanarroyes.apichat.security.UserPrincipal;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
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

    /**
     *
     * @param userObj
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody UserObj userObj){

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        AuthResponse response = null;

        try {
            response = getAuthResponse(userObj.getEmail(), userObj.getPassword());
            httpStatus = HttpStatus.OK;
        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch (Exception ex) {
            log.error("Unexpected error in method authenticateUser", ex);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     *
     * @param userObj
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserObj userObj) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        AuthResponse authResponse = null;

        try {
            if(userObj.getEmail() == null || userObj.getEmail().equals("")){
               throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            if(userObj.getPassword() == null || userObj.getPassword().equals("")){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            if(userService.existsByUsername(userObj.getEmail())){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            User user = new User();
            user.setEmail(userObj.getEmail());
            user.setPassword(passwordEncoder.encode(userObj.getPassword()));
            User result = userRepository.save(user);
            authResponse = getAuthResponse(userObj.getEmail(), userObj.getPassword());
            httpStatus = HttpStatus.CREATED;
        } catch (HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        } catch (Exception ex) {
            log.error("Unexpected error in method registerUser", ex);
        }
        return new ResponseEntity<>(authResponse, httpStatus);
    }

    /**
     *
     * @param refreshTokenRequest
     * @return
     */
    @PostMapping("/token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenRepository.findById(refreshTokenRequest.getRefreshToken()).map(refreshToken -> {
            User user = refreshToken.getUser();
            String accessToken = tokenService.generateToken(UserPrincipal.create(user));
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken(), jwtExpirationInMs));
        }).orElseThrow(() -> new BadRequestException("Invalid Refresh Token"));
    }

    /**
     *
     * @param email
     * @param password
     * @return
     * @throws HttpClientErrorException
     */
    private AuthResponse getAuthResponse(String email, String password) throws HttpClientErrorException{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
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

        return new AuthResponse(accessToken, refreshToken, jwtExpirationInMs);
    }
}
