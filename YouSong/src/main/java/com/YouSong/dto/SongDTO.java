package com.YouSong.dto;

import jakarta.persistence.*;
import lombok.*;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class SongDTO {
        private long id;
        private String title;
        private String artist;
        private String genre;
        private double length;
}
