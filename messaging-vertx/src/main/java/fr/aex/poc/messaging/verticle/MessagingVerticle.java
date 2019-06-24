package fr.aex.poc.messaging.verticle;

import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventBuilder;
import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.reactivex.core.AbstractVerticle;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class MessagingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MessagingVerticle.class);

    public void start() {

        vertx.createHttpServer()
                .requestHandler(req -> VertxCloudEvents.create().rxReadFromRequest(req)
                        .subscribe((receivedEvent, throwable) -> {
                            if (receivedEvent != null) {

                                System.out.println("The event type: " + receivedEvent.getType());
                                System.out.println("The content: " + receivedEvent.getData());

                            }
                        }))
                .rxListen(8080)
                .subscribe(server -> {
                    System.out.println("Server running!");
                });
    }


    void launch() {
        final HttpClientRequest request = Vertx.vertx().createHttpClient().post(8080, "localhost", "/");


        Event event = new Event();
        event.message = "sfsdf";

        final CloudEvent<Event> cloudEvent = new CloudEventBuilder<Event>()
                .type("sdf")
                .id("dsf")
                .source(URI.create("/trigger"))
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

