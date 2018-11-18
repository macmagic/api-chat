package com.juanarroyes.apichat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class AllowAnonymousWebAccess extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity web) throws Exception {
            web.cors().and().csrf().disable()
                .authorizeRequests()
                .anyRequest().anonymous();
    }
}
