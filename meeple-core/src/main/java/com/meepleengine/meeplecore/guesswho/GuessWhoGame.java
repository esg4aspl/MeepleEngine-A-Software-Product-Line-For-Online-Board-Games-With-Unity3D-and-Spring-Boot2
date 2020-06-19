package com.meepleengine.meeplecore.guesswho;

import com.meepleengine.meeplecore.model.*;

import java.util.List;

public class GuessWhoGame extends Game {

    private PlayerColor turnColor;
    private Deck drawPile;
    private Card redMysteryCard;
    private Card blueMysteryCard;

    public GuessWhoGame(String id,
                        List<Player> players,
                        TurnState turnState,
                        Board board,
                        Deck drawPile,
                        Card redMysteryCard,
                        Card blueMysteryCard) {
        super(id, players, turnState, board);
        this.drawPile = drawPile;
        this.redMysteryCard = redMysteryCard;
        this.blueMysteryCard = blueMysteryCard;
    }

    public PlayerColor getTurnColor() {
        return turnColor;
    }

    public void setTurnColor(PlayerColor turnColor) {
        this.turnColor = turnColor;
    }

    public Card getRedMysteryCard() {
        return redMysteryCard;
    }

    public Card getBlueMysteryCard() {
        return blueMysteryCard;
    }
}

