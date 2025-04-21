package com.YouSong.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "benutzer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Benutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String token;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Song> songs = new ArrayList<>();

    public boolean verifyPassword(String rawPassword){
        return BCrypt.checkpw(rawPassword, this.password);
    }
}
