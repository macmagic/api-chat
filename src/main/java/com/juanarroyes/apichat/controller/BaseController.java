package com.juanarroyes.apichat.controller;

import com.juanarroyes.apichat.exception.UserNotFoundException;
import com.juanarroyes.apichat.model.User;
import com.juanarroyes.apichat.service.TokenService;
import com.juanarroyes.apichat.service.TokenServiceImpl;
import com.juanarroyes.apichat.service.UserService;
import com.juanarroyes.apichat.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class BaseController {

    protected TokenService tokenService;

    private UserService userService;

    @Autowired
    public BaseController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    protected User getUserFromToken() {

        User user = null;
        Long userId = null;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String accessToken = HttpUtils.getAccessTokenFromRequest(request);
            userId = tokenService.getUserIdByToken(accessToken);
            user = userService.getUser(userId);
        } catch (UserNotFoundException e) {
            log.error("Cannot found user with id: " + userId);
        } catch (Exception e) {
            log.error("Unexpected error in method getUserFromToken");
        }
        return user;
    }


}
