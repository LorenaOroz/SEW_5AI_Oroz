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
		Song song1 = Song.builder()
				.title("Sailor Song")
				.artist("Gigi Perez")
				.genre("Indie folk")
				.length(3.5167).build();

		Song song2 = Song.builder()
				.title("TV")
				.artist("Billie Eillish")
				.genre("Pop")
				.length(4.6833).build();

		Song song3 = Song.builder()
				.title("Crazy")
				.artist("Shawn Mendes")
				.genre("Pop")
				.length(3.1833).build();

		songRepository.save(song1);
		songRepository.save(song2);
		songRepository.save(song3);
	}
}
