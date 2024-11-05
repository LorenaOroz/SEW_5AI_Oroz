package com.YouSong.repository;
import com.YouSong.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
public interface SongRepository extends JpaRepository<Song, Long>{

}
