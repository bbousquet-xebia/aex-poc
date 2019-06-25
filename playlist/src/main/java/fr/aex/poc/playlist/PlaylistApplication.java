package fr.aex.poc.playlist;

import fr.aex.poc.playlist.objects.Podcast;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class PlaylistApplication {

	@RestController
	class ReferentielController {
		@GetMapping("/podcasts")
		List<Podcast> listPodcasts() throws IOException {
			return PlaylistMethods.listPodcasts("");
		}

		@GetMapping("/version")
		String version() throws IOException {
			return PlaylistMethods.getVersion();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PlaylistApplication.class, args);
	}
}
