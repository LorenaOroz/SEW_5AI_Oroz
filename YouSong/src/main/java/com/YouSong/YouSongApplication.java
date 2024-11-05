package  com.YouSong;

import com.YouSong.entity.Song;
import com.YouSong.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YouSongApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(YouSongApplication.class, args);
	}

	@Autowired
	private SongRepository songRepository;
	@Override
	public void run(String... args) throws Exception {

	}
}
