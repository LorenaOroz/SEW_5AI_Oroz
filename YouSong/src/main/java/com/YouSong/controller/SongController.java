package com.YouSong.controller;

import com.YouSong.entity.Song;
import com.YouSong.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {
    @Autowired
    private SongRepository songRepository;

    @GetMapping("/songs")
    public List<Song> fetchSongs() {
        return songRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        return songOptional.map(song -> ResponseEntity.ok().body(song))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/songs")
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        song.setId(0);
        Song savedSong = songRepository.save(song);
        return ResponseEntity.ok(savedSong);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song updatedSong) {
        Optional<Song> existingSongOptional = songRepository.findById(id);
        if (existingSongOptional.isPresent()) {
            Song existingSong = existingSongOptional.get();
            existingSong.setTitle(updatedSong.getTitle());
            existingSong.setArtist(updatedSong.getArtist());
            existingSong.setGenre(updatedSong.getGenre());
            existingSong.setLength(updatedSong.getLength());
            songRepository.save(existingSong);
            return ResponseEntity.ok(existingSong);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
