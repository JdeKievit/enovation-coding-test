package com.jelco.enovationcodingtest;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    final static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                //.requestMatchers("/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/home", "/about").permitAll()
                .requestMatchers(HttpMethod.POST, "/account").permitAll()
                .requestMatchers(HttpMethod.PUT, "/account/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/accounts", "/account/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/actuator/**", "/h2/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/h2/**").hasAnyRole("ADMIN"))
            .formLogin((form) -> form.loginPage("/login").permitAll())
            .logout((logout) -> logout.permitAll())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String userPassword = RandomStringUtils.random(8, true, true);
        String adminPassword = RandomStringUtils.random(8, true, true);
        logger.info("Password user: " + userPassword);
        logger.info("Password admin: " + adminPassword);
        UserDetails user = User.withUsername("user")
                .password(encoder.encode(userPassword))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}