package com.meepleengine.meeplecore.model;

import java.util.List;

public class PlayerHand implements Item {

    private String id;
    private Grid grid;
    private Grid currentGrid;

    public PlayerHand(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCurrentGrid(Grid currentGrid) {
        this.currentGrid = currentGrid;
    }

    public List<Item> getCards() {
        return grid.getItemList();
    }

    public void addCards(List<Card> cards) {
        for (Card card: cards) {
            addCard(card);
        }
    }

    public void addCard(Card card) {
        grid.addItem(card);
    }

    public void removeCard(Card card) {
        grid.removeItem(card);
    }

    public Card popCard(String cardId) {
        for (Item item: getCards()) {
            if (item.getId().equals(cardId)) {
                Card card = (Card) item;
                removeCard(card);
                return card;
            }
        }
        return null;
    }
}
