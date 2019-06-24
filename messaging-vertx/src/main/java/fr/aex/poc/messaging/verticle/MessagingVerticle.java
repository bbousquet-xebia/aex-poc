package fr.aex.poc.messaging.verticle;

import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.reactivex.core.AbstractVerticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MessagingVerticle.class);

    public void start() {

        vertx.createHttpServer()
                .requestHandler(req -> VertxCloudEvents.create().rxReadFromRequest(req)
                        .subscribe((receivedEvent, throwable) -> {
                            if (receivedEvent != null) {
                                // I got a CloudEvent object:
                                System.out.println("The event type: " + receivedEvent.getType());
                            }
                        }))
                .rxListen(8080)
                .subscribe(server -> {
                    System.out.println("Server running!");
                });
    }

}

