package com.meepleengine.meeplecore.dixit;

import com.meepleengine.meeplecore.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DixitGame extends Game {

    // turn
    private int turnNumber;
    private Player turnPlayer;
    private List<Player> activePlayers;

    // helpers
    private Deck shufflePile;
    private Deck drawPile;
    private Deck discardPile;
    private Map<PlayerItem, Grid> votingTokensMap;
    private Map<Player, Integer> scoreboardMap;

    public DixitGame(String id,
                     List<Player> players,
                     TurnState turnState,
                     Board board,
                     Deck drawPile,
                     Deck shufflePile,
                     Deck discardPile) {
        super(id, players, turnState, board);
        this.drawPile = drawPile;
        this.shufflePile = shufflePile;
        this.scoreboardMap = initializeScoreboardMap();
        this.discardPile = discardPile;
        this.votingTokensMap = new HashMap<>();
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(List<Player> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public Deck getShufflePile() {
        return shufflePile;
    }

    public void setShufflePile(Deck shufflePile) {
        this.shufflePile = shufflePile;
    }

    public Map<PlayerItem, Grid> getVotingTokensMap() {
        return votingTokensMap;
    }

    public void setVotingTokensMap(Map<PlayerItem, Grid> votingTokensMap) {
        this.votingTokensMap = votingTokensMap;
    }

    public Deck getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(Deck drawPile) {
        this.drawPile = drawPile;
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Deck discardPile) {
        this.discardPile = discardPile;
    }

    public Map initializeScoreboardMap() {
        Map scoreboardMap = new HashMap();
        for (Player player: getPlayers()) {
            scoreboardMap.put(player, player.getScore());
        }
        return scoreboardMap;
    }

    public void calculateScore(Map shufflePileMap) {
        int count = 0;
        int point;
        for (PlayerItem item: this.votingTokensMap.keySet()) {
            Integer id = item.getNo();
            Player votingPlayer = item.getPlayer();
            Board cardRevealBoard = (Board) getBoard().getGrids().get(12).getItemList().get(0);
            List<Grid> cardRevealBoardGrids = cardRevealBoard.getGrids();
            Card card = (Card) cardRevealBoardGrids.get(id).getItemList().get(0);
            Player cardOwner = (Player) shufflePileMap.get(card); // the owner of the card
            if (cardOwner == turnPlayer) {
                count++;
                point = scoreboardMap.get(votingPlayer) + 3;
                scoreboardMap.put(votingPlayer, point); // votingPlayer found the storytellers card, point 3
            } else {
                point = scoreboardMap.get(cardOwner) + 1;
                scoreboardMap.remove(cardOwner);
                scoreboardMap.put(cardOwner, point);
            }
        }

        if (count == getNumberOfPlayers() - 1) { // everybody found the storytellers card
            // storyteller gets 0, already given 3 to others
        } else if (count == 0) { // nobody found the storyteller's card
            for (Player player : getPlayers()) {
                if (player != getTurnPlayer()) {
                    point = scoreboardMap.get(player) + 2;
                    scoreboardMap.remove(player);
                    scoreboardMap.put(player, point);
                }
            }
        } else {
            point = scoreboardMap.get(turnPlayer) + 3;
            scoreboardMap.remove(turnPlayer);
            scoreboardMap.put(turnPlayer, point);
        }

        for (Map.Entry<Player,Integer> entry: scoreboardMap.entrySet()) {
            entry.getKey().setScore(entry.getValue());
            // to test when score is = 30, game is over
//            if (entry.getKey().getId() == "elo1") {
//                entry.getKey().setScore(45);
//            }
            System.out.println("Key = " + entry.getKey().getId() + ", Value = " + entry.getValue());
        }
    }

    public List<Player> announceWinners() {
        int max = getPlayers().get(0).getScore();
        List<Player> winners = new ArrayList<>();
        for (Player player: getPlayers()) {
            if (player.getScore() >= max) {
                max = player.getScore();
                winners.add(player);
            }
        }
        return winners;
    }
}