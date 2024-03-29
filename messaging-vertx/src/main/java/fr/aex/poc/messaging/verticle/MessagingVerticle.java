package fr.aex.poc.messaging.verticle;

import com.google.pubsub.v1.PubsubMessage;
import fr.aex.poc.common.daos.DatastoreDao;
import fr.aex.poc.common.daos.PodcastDao;
import fr.aex.poc.common.objects.Podcast;
import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventBuilder;
import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.reactivex.core.AbstractVerticle;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Base64;

public class MessagingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MessagingVerticle.class);

    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

    private Podcast deserializeMessage(String message) {
        try {
            PubSubMessage messageObject = mapper.readValue(message, PubSubMessage.class);

            byte[] decodedBytes = Base64.getDecoder().decode(messageObject.getData());
            String decodedString = new String(decodedBytes);

            return mapper.readValue(
                    decodedString,
                    Podcast.class
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void start() {

        vertx.createHttpServer()
                .requestHandler(req -> VertxCloudEvents.create().<String>rxReadFromRequest(req)
                        .subscribe((receivedEvent, throwable) -> {
                            if (receivedEvent != null) {

                                PodcastDao dao = new DatastoreDao();

                                receivedEvent.getData().ifPresent(message -> {
                                                System.out.println("Message received : " + message);

                                            try {
                                                dao.createPodcast(deserializeMessage(message));
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                );

                            }
                        }))
                .rxListen(8080)
                .subscribe(server -> {
                    System.out.println("Server running!");
                });
    }


    void launch() {
        final HttpClientRequest request = Vertx.vertx().createHttpClient().post(8080, "localhost", "/");


        Podcast event = new Podcast.Builder()
                .author("sdfsdfd")
                .title("sdfsdfsdf")
                .description("dsfsdfsdf")
                .build();

//        {"ID":"640313443588257","Data":"eyJtZXNzYWdlIjoiR3JycnJycnJycnIgISJ9","Attributes":null,"PublishTime":"2019-06-24T15:16:03.683Z"}

        final CloudEvent<Podcast> cloudEvent = new CloudEventBuilder<Podcast>()
                .type("aex.poc.podcast")
                .id("dsf")
                .source(URI.create("/podcast"))
                .data(event)
                .build();

// add a client response handler
        request.handler(resp -> {
            // react on the server response
        });

// write the CloudEvent to the given HTTP Post request object
        VertxCloudEvents.create().writeToHttpClientRequest(cloudEvent, request);
        request.end();

    }
}

