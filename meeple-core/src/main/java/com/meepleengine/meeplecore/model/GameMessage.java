package com.meepleengine.meeplecore.model;
import com.fasterxml.jackson.databind.JsonNode;

public class GameMessage {

    private String name;
    private JsonNode data;

    public GameMessage(String name, JsonNode data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public JsonNode getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GameMessage{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}
