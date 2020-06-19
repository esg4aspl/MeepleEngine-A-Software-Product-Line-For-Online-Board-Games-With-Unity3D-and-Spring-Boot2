package com.meepleengine.meeplecore.model;

public class PlayerItem implements Item {

    private String id;
    private Player player;
    private Boolean isRevealed;
    private Grid currentGrid;
    private Grid grid;
    private int no;

    public PlayerItem(String id) {
        this.id = id;
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

    public void setCurrentGrid(Grid grid) {
        this.currentGrid = grid;
    }

    @Override
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Boolean isRevealed() {
        return isRevealed;
    }

    public Boolean getRevealed() {
        return isRevealed;
    }

    public void setRevealed(Boolean revealed) {
        isRevealed = revealed;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
