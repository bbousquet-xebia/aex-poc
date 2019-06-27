package fr.aex.poc.messaging.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PubSubMessage {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("Data")
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @JsonProperty("Attributes")
    private String attributes;

    @JsonProperty("PublishTime")
    private String publishTime;
}
