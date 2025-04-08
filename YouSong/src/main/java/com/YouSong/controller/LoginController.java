package com.YouSong.controller;
import com.YouSong.entity.Benutzer;
import com.YouSong.repository.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class LoginController {

    @Autowired
    private BenutzerRepository benutzerRepository;

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class BenutzerResponse {
        public String token;
    }

    @PostMapping("/api/login")
    public ResponseEntity<BenutzerResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByUsername(loginRequest.username);
        System.out.println(optionalBenutzer.isPresent());
        if (optionalBenutzer.isPresent()) {
            Benutzer benutzer = optionalBenutzer.get();
            if (benutzer.verifyPassword(loginRequest.password)) {
                BenutzerResponse benutzerResponse = new BenutzerResponse();
                benutzerResponse.token = benutzer.getToken();
                return ResponseEntity.ok().body(benutzerResponse);
            }
        }
        return new ResponseEntity<BenutzerResponse>(HttpStatus.UNAUTHORIZED);
}
}