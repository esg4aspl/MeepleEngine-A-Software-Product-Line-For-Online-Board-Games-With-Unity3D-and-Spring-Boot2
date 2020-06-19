package com.meepleengine.meeplecore.codenames;

public enum CardType
{
    RED_AGENT(1),
    BLUE_AGENT(2),
    INNOCENT(3),
    ASSASSIN(4);

    private int id;

    CardType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CardType getCardType(int id) {
        for (CardType cardType: CardType.values()) {
            if (cardType.getId() == id) {
                return cardType;
            }
        }
        return null;
    }
}
