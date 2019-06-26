package fr.aex.poc.common.daos;

import fr.aex.poc.common.objects.Podcast;
import fr.aex.poc.common.objects.Result;

import java.sql.SQLException;

public interface PodcastDao {
    Long createPodcast(Podcast playlist) throws SQLException;

    Podcast readPodcast(Long bookId) throws SQLException;

    void updatePodcast(Podcast book) throws SQLException;

    void deletePodcast(Long bookId) throws SQLException;

    Result<Podcast> listPodcasts(String startCursor) throws SQLException;

}
