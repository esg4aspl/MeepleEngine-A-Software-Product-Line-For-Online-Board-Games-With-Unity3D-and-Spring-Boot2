package com.meepleengine.meeplecore.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private String id;
    private double posX;
    private double posY;
    private List<Item> itemList;
    private Item parent;

    public Grid(String id) {
        this.id = id;
        this.itemList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Item findById(String itemId) {
        for (Item item: getItemList()) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public void addItem(Item item) {
        item.setCurrentGrid(this);
        itemList.add(item);
    }

    public void removeItem(Item item) {
        item.setCurrentGrid(null);
        itemList.remove(item);
    }
}
