package com.meepleengine.meeplecore.model;
import com.fasterxml.jackson.databind.JsonNode;

public class PlayerMessage {

    private Player player;
    private String name;
    private JsonNode data;

    public PlayerMessage(Player player, String name, JsonNode data) {
        this.player = player;
        this.name = name;
        this.data = data;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public JsonNode getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PlayerMessage{" +
                "player=" + this.player.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", data=" + this.getData() +
                '}';
    }
}
