package com.YouSong.repository;

import com.YouSong.entity.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenutzerRepository extends JpaRepository<Benutzer, Long> {
    Optional<Benutzer> findByToken(String username);
    Optional<Benutzer> findByUsername(String username);
}
