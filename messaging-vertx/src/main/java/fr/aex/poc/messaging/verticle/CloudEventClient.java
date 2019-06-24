package fr.aex.poc.messaging.verticle;

import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventBuilder;
import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClientRequest;

import java.net.URI;

public class CloudEventClient {
    public static void main(String[] args) {
        new MessagingVerticle().launch();
    }

}
