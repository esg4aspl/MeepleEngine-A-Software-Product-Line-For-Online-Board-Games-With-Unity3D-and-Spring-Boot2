package com.meepleengine.meeplecore.model;

import com.meepleengine.meeplecore.api.IGame;
import com.meepleengine.meeplecore.api.IGameObserver;

import java.util.List;

public class Game implements IGame {

    // game
    private String id;
    private List<Player> players;
    private TurnState turnState;
    private Board board;
    private IGameObserver IGameObserver;

    public Game(String id, List<Player> players, TurnState turnState, Board board) {
        this.id = id;
        this.players = players;
        this.turnState = turnState;
        this.board = board;
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
    }

    public Board getBoard() {
        return board;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void onAction(GameMessage gameMessage) {
        IGameObserver.onAction(gameMessage);
    }

    @Override
    public void registerObserver(IGameObserver IGameObserver) {
        this.IGameObserver = IGameObserver;
    }
}
