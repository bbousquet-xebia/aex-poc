package fr.aex.poc.common.daos;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import fr.aex.poc.common.objects.Podcast;
import fr.aex.poc.common.objects.Result;

import java.util.ArrayList;
import java.util.List;

public class DatastoreDao implements PodcastDao {

    // [START constructor]
    private Datastore datastore;
    private KeyFactory keyFactory;

    public DatastoreDao() {
        datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
        keyFactory = datastore.newKeyFactory().setKind("Podcast2");      // Is used for creating keys later
    }

    // [END constructor]

    // [START entityToPodcast]
    public Podcast entityToPodcast(Entity entity) {
        return new Podcast.Builder()
                .author(entity.getString(Podcast.AUTHOR))
                .description(entity.getString(Podcast.DESCRIPTION))
                .id(entity.getKey().getId())
                .publishedDate(entity.getString(Podcast.PUBLISHED_DATE))
                .title(entity.getString(Podcast.TITLE))
                .build();
    }

    // [END entityToPodcast]
    // [START create]
    @Override
    public Long createPodcast(Podcast podcast) {
        IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
        FullEntity<IncompleteKey> incPodcastEntity = Entity.newBuilder(key)  // Create the Entity
                .set(Podcast.AUTHOR, podcast.getAuthor())           // Add Property ("author", book.getAuthor())
                .set(Podcast.DESCRIPTION, podcast.getDescription())
                .set(Podcast.PUBLISHED_DATE, podcast.getPublishedDate())
                .set(Podcast.TITLE, podcast.getTitle())
                .build();
        Entity bookEntity = datastore.add(incPodcastEntity); // Save the Entity
        return bookEntity.getKey().getId();                     // The ID of the Key
    }
    // [END create]

    // [START read]
    @Override
    public Podcast readPodcast(Long podcastId) {
        Entity bookEntity = datastore.get(keyFactory.newKey(podcastId)); // Load an Entity for Key(id)
        return entityToPodcast(bookEntity);
    }
    // [END read]

    // [START update]
    @Override
    public void updatePodcast(Podcast podcast) {
        Key key = keyFactory.newKey(podcast.getId());  // From a book, create a Key
        Entity entity = Entity.newBuilder(key)         // Convert Podcast to an Entity
                .set(Podcast.AUTHOR, podcast.getAuthor())
                .set(Podcast.DESCRIPTION, podcast.getDescription())
                .set(Podcast.PUBLISHED_DATE, podcast.getPublishedDate())
                .set(Podcast.TITLE, podcast.getTitle())
                .build();
        datastore.update(entity);                   // Update the Entity
    }
    // [END update]

    // [START delete]
    @Override
    public void deletePodcast(Long podcastId) {
        Key key = keyFactory.newKey(podcastId);        // Create the Key
        datastore.delete(key);                      // Delete the Entity
    }
    // [END delete]

    // [START entitiesToPodcasts]
    public List<Podcast> entitiesToPodcasts(QueryResults<Entity> resultList) {
        List<Podcast> resultPodcasts = new ArrayList<>();
        while (resultList.hasNext()) {  // We still have data
            resultPodcasts.add(entityToPodcast(resultList.next()));      // Add the Podcast to the List
        }
        return resultPodcasts;
    }


    @Override
    public Result<Podcast> listPodcasts(String startCursorString) {
        Cursor startCursor = null;
        if (startCursorString != null && !startCursorString.equals("")) {
            startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
        }
        Query<Entity> query = Query.newEntityQueryBuilder()       // Build the Query
                .setKind("Podcast2")                                        // Only show 10 at a time
                .setStartCursor(startCursor)               // Use default Index "title"
                .build();
        QueryResults<Entity> resultList = datastore.run(query);   // Run the query
        List<Podcast> resultPodcasts = entitiesToPodcasts(resultList);     // Retrieve and convert Entities
        Cursor cursor = resultList.getCursorAfter();              // Where to start next time
        if (cursor != null && resultPodcasts.size() == 10) {         // Are we paging? Save Cursor
            String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
            return new Result<>(resultPodcasts, cursorString);
        } else {
            return new Result<>(resultPodcasts);
        }
    }
    // [END listbooks]
}

