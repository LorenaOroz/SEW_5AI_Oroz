package com.YouSong.service;

import com.YouSong.entity.Benutzer;
import com.YouSong.repository.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class BenutzerDetailsService implements UserDetailsService {

    @Autowired
    private BenutzerRepository benutzerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Benutzer> benutzerOpt = benutzerRepository.findByUsername(username);

        if (!benutzerOpt.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Benutzer benutzer = benutzerOpt.get();

        return new User(
                benutzer.getUsername(),
                benutzer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}