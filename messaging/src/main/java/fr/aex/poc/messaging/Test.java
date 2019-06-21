package fr.aex.poc.messaging;

import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventBuilder;

import java.net.URI;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        final String eventId = UUID.randomUUID().toString();
        final URI src = URI.create("/trigger");
        final String eventType = "My.Cloud.Event.Type";
        final Event payload = new Event();
        payload.message = "Toto";

// passing in the given attributes
        final CloudEvent<Event> cloudEvent = new CloudEventBuilder<Event>()
                .type(eventType)
                .id(eventId)
                .source(src)
                .data(payload)
                .build();
    }
}
