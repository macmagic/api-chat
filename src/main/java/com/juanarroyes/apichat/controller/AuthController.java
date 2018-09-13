package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.dto.UserObj;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.repository.UserRepository;
import com.juanarroyes.apichat.response.JwtAuthResponse;
import com.juanarroyes.apichat.security.JwtTokenProvider;
import com.juanarroyes.apichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthenticationManager authenticationManager;

    UserService userService;

    PasswordEncoder passwordEncoder;

    JwtTokenProvider jwtTokenProvider;

    UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserObj userObj){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userObj.getEmail(), userObj.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider .generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthResponse(jwt));
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

}
