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

    @Autowired
    private BenutzerRepository benutzerRepository;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            throw new BadCredentialsException("Missing API Key");
        }
        Benutzer user = benutzerRepository.findByToken(apiKey)
                .orElseThrow(() -> new BadCredentialsException("Invalid API Key"));
        return new ApiKeyAuthentication(user.getUsername(), AuthorityUtils.NO_AUTHORITIES);
    }

}
