package com.meepleengine.meeplecore.model;

import java.util.ArrayList;
import java.util.List;

public class Deck implements Item{

    private String id;
    private Grid currentGrid;
    private Grid grid;

    public Deck(String id) {
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

    @Override
    public void setCurrentGrid(Grid grid) {
        this.currentGrid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
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

    public Card draw() {
        int index = grid.getItemList().size() - 1;
        Card card = (Card) grid.getItemList().remove(index);
        card.setCurrentGrid(null);
        return card;
    }

    public Card findById(String cardId) {
        Item item = grid.findById(cardId);
        return (Card) item;
    }

    public List<Card> draw(int number) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int index = grid.getItemList().size() - 1;
            Card card = (Card) grid.getItemList().remove(index);
            card.setCurrentGrid(null);
            cards.add(card);
        }
        return cards;
    }
}
