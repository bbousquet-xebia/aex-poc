package fr.aex.poc.playlist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import fr.aex.poc.playlist.daos.DatastoreDao;
import fr.aex.poc.playlist.daos.PodcastDao;
import fr.aex.poc.playlist.objects.Podcast;
import fr.aex.poc.playlist.objects.Result;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMethods {

    public static String getProjectId() {
        return System.getenv("PROJECT_ID");
    }

    public static String getVersion() {
        return System.getenv("PROJECT_VERSION");
    }

    public static String getProjectBucket() {
        return System.getenv("PROJECT_BUCKET");
    }

    public static String getRefPath(String refName) {
        return System.getenv("REFS_FOLDER") + "/" + refName;
    }

    public static String getCredentialsPath() {
        return System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
    }

    public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }

    public static List<Podcast> listPodcasts(String startCursor) throws IOException {

        PodcastDao dao = new DatastoreDao();

        List<Podcast> podcasts = new ArrayList<>();
        String endCursor = null;
        try {
            Result<Podcast> result = dao.listPodcasts(startCursor);
            podcasts = result.result;
            endCursor = result.cursor;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception !" + e);
        }

        podcasts.addAll(podcasts);

        return podcasts;
    }
}
