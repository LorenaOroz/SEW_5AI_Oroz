package com.YouSong.controller;

import com.YouSong.dto.SongDTO;
import com.YouSong.entity.Song;
import com.YouSong.entity.Benutzer;                      // neu
import com.YouSong.projection.SongFileProjection;
import com.YouSong.repository.SongRepository;
import com.YouSong.repository.BenutzerRepository;        // neu
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // neu

import java.security.Principal;                         // neu
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {
    @Autowired
    private SongRepository songRepository;

    @Autowired                                         // neu
    private BenutzerRepository benutzerRepository;     // neu

    @GetMapping("/songs")
    public List<SongDTO> fetchSongs() {
        return songRepository.findAll().stream()
                .map(song -> new SongDTO(song.getId(), song.getTitle(), song.getArtist(), song.getGenre(), song.getLength()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        return songOptional.map(song -> ResponseEntity.ok().body(song))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/songs")
    public ResponseEntity<Song> createSong(
            @RequestBody Song song,
            Principal principal                            // neu
    ) {
        // a) Owner aus Principal ermitteln
        Benutzer owner = benutzerRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unbekannter Nutzer"));
        // b) ID zurücksetzen und Owner setzen
        song.setId(0);
        song.setOwner(owner);
        // c) Speichern
        Song savedSong = songRepository.save(song);
        return ResponseEntity.ok(savedSong);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(
            @PathVariable Long id,
            @RequestBody Song updatedSong,
            Principal principal                            // neu
    ) {
        Song existingSong = songRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Song nicht gefunden"));

        // Owner‑Check
        if (!existingSong.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Du bist nicht der Besitzer dieses Songs");
        }

        // Feld‑Updates
        existingSong.setTitle(updatedSong.getTitle());
        existingSong.setArtist(updatedSong.getArtist());
        existingSong.setGenre(updatedSong.getGenre());
        existingSong.setLength(updatedSong.getLength());
        if (updatedSong.getFileData() != null && !updatedSong.getFileData().isEmpty()) {
            existingSong.setFileData(updatedSong.getFileData());
        }

        songRepository.save(existingSong);
        return ResponseEntity.ok(existingSong);
    }


    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(
            @PathVariable Long id,
            Principal principal                            // neu
    ) {
        Song existingSong = songRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Song nicht gefunden"));

        // Owner‑Check
        if (!existingSong.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Du bist nicht der Besitzer dieses Songs");
        }

        songRepository.delete(existingSong);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/songs/search")
    public List<Song> searchSongs(@RequestParam String query) {
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(query, query);
    }

    @GetMapping("/songs/{id}/play")
    public ResponseEntity<String> getSongFileData(@PathVariable Long id) {
        SongFileProjection songFile = songRepository.findFileDataById(id);
        if (songFile != null) {
            return ResponseEntity.ok(songFile.getFileData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
