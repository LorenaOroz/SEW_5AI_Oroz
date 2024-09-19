package com.YouSong.controller;

import com.YouSong.entity.Song;
import com.YouSong.repository.SongRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173/")
public class SongController {
    @Autowired
    private SongRepository songRepository;
    @GetMapping("/songs")
    public List<Song> fetchSongs(){
        return songRepository.findAll();
    }
}
