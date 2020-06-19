package com.meepleengine.meeplecore.model;
import com.fasterxml.jackson.databind.JsonNode;
import com.meepleengine.meeplecore.api.IPlayer;
import com.meepleengine.meeplecore.api.IPlayerObserver;
import org.kie.api.runtime.KieSession;

import java.util.Map;

public class Player implements IPlayer {

    private String id;
    private Role role;
    private PlayerColor color;
    private PlayerHand playerHand;
    private PlayerMessage playerMessage;
    private Map<String, PlayerItem> playerItemMap;
    private IPlayerObserver IPlayerObserver;
    private int score;
    private KieSession kieSession;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(PlayerHand playerHand) {
        this.playerHand = playerHand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PlayerMessage getPlayerMessage() {
        return playerMessage;
    }

    public void setPlayerMessage(PlayerMessage playerMessage) {
        this.playerMessage = playerMessage;
    }

    public Map<String, PlayerItem> getPlayerItemMap() {
        return playerItemMap;
    }

    public void setPlayerItemMap(Map<String, PlayerItem> playerItemSet) {
        this.playerItemMap = playerItemSet;
    }

    public PlayerItem getPlayerItem(String playerItemId) {
        for (PlayerItem playerItem: playerItemMap.values()) {
            if (playerItem.getId().equals(playerItemId)) {
                return playerItem;
            }
        }
        return null;
    }

    public void onAction(PlayerMessage playerMessage) {
        IPlayerObserver.onAction(playerMessage);
    }

    @Override
    public void action(String name, JsonNode actionDetails) {
        PlayerMessage playerMessage = new PlayerMessage(this, name, actionDetails);
        kieSession.insert(playerMessage);
    }

    @Override
    public void registerObserver(IPlayerObserver IPlayerObserver) {
        this.IPlayerObserver = IPlayerObserver;
    }

    public void setKieSession(KieSession kieSession) {
        this.kieSession = kieSession;
    }
}
