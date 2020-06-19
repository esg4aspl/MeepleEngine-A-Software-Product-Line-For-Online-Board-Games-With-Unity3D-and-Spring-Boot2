package com.meepleengine.meeplecore.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Card implements Item {

    private String id;
    private Boolean isRevealed;
    private JsonNode data;
    private Grid currentGrid;
    private Grid grid;

    public Card(Grid grid) {
        this.grid = grid;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Grid getCurrentGrid() {
        return currentGrid;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(String field, String data) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode().put(field, data);
        this.data = node;
    }

    @Override
    public void setCurrentGrid(Grid currentGrid) {
        this.currentGrid = currentGrid;
    }

    @Override
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public Boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(Boolean revealed) {
        isRevealed = revealed;
    }
}
