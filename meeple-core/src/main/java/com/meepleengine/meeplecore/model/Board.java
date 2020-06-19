package com.meepleengine.meeplecore.model;

import java.util.ArrayList;
import java.util.List;

public class Board implements Item {

    private String id;
    private List<Grid> grids;
    private Grid grid;

    public Board(String id) {
        this.id = id;
        this.grids = new ArrayList<>();
    }

    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setCurrentGrid(Grid grid) {

    }

    @Override
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void addGrid(Grid grid) {
        this.grids.add(grid);
    }

    public Item findById(String itemId) {
        for (Grid grid: getGrids()) {
            Item item = grid.findById(itemId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
}
