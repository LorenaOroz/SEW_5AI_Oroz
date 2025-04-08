package com.YouSong.service;

import com.YouSong.ApiKeyAuthentication;
import com.YouSong.entity.Benutzer;
import com.YouSong.repository.BenutzerRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String AUTH_TOKEN_PREFIX = "Baeldung ";

    @Autowired
    private BenutzerRepository benutzerRepository;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        System.out.println(apiKey);

        if (apiKey == null || !apiKey.startsWith(AUTH_TOKEN_PREFIX)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        String token = apiKey.substring(AUTH_TOKEN_PREFIX.length());
        Benutzer user = benutzerRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid API Key"));

        return new ApiKeyAuthentication(user.getUsername(), AuthorityUtils.NO_AUTHORITIES);
}
}
