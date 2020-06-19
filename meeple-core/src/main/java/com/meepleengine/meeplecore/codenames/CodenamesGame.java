package com.meepleengine.meeplecore.codenames;

import com.meepleengine.meeplecore.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodenamesGame extends Game {

    private PlayerColor turnColor;
    private Deck drawPile;
    private Deck words;
    private Map keyMap;
    private Integer guessNumber;
    private Deck redAgentsDeck;
    private Deck blueAgentsDeck;

    public CodenamesGame(String id,
                         List<Player> players,
                         TurnState turnState,
                         Board board,
                         Deck drawPile,
                         Deck words,
                         Map keyMap,
                         Deck blueAgentsDeck,
                         Deck redAgentsDeck) {
        super(id, players, turnState, board);
        this.drawPile = drawPile;
        this.words = words;
        this.keyMap = keyMap;
        this.redAgentsDeck = redAgentsDeck;
        this.blueAgentsDeck = blueAgentsDeck;
        this.turnColor = assignTurnColor();
        this.guessNumber = 0;
    }

    public PlayerColor getTurnColor() {
        return turnColor;
    }

    public void setTurnColor(PlayerColor turnColor) {
        this.turnColor = turnColor;
    }

    public Integer getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(Integer guessNumber) {
        this.guessNumber = guessNumber;
    }

    public Deck getDrawPile() {
        return drawPile;
    }

    public Deck getWords() {
        return words;
    }

    public Map getKeyMap() {
        return keyMap;
    }

    public Deck getRedAgentsDeck() {
        return redAgentsDeck;
    }

    public Deck getBlueAgentsDeck() {
        return blueAgentsDeck;
    }

    private PlayerColor assignTurnColor() {
        Map<CardType, Integer> resultMap = new HashMap<>();
        for (Object key: keyMap.keySet()) {
            CardType type = (CardType) keyMap.get(key);

            if (resultMap.containsKey(type)) {
                resultMap.put(type, resultMap.get(type) + 1);
            } else {
                resultMap.put(type, 1);
            }
        }

        for(Object key: resultMap.keySet()) {
            if(resultMap.get(key).equals(9)) {
                CardType type = (CardType) key;
                if (type == CardType.BLUE_AGENT) {
                    return PlayerColor.BLUE;
                } else {
                    return PlayerColor.RED;
                }
            }
        }

        return null;
    }
}
