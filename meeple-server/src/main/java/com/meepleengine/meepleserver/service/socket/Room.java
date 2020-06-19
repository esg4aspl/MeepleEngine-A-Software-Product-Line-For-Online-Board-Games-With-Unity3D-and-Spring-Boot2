package com.meepleengine.meepleserver.service.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.model.Game;
import com.meepleengine.meeplecore.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Room {

    private final Logger logger = LoggerFactory.getLogger(Room.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String id;
    private GameContainer gameContainer;
    private Map<String, WebSocketSession> playerIdWebSocketSessions = new HashMap<>();
    private boolean running = false;

    public Room(String id, GameContainer gameContainer) {
        this.id = id;
        this.gameContainer = gameContainer;
    }

    public void registerObservers() {
        Game game = gameContainer.getGame();
        List<Player> playerList = game.getPlayers();

        playerList.forEach(player -> {
            player.registerObserver(playerMessage -> {
                logger.info("RoomId: {}, PlayerId: {}, PlayerMessage: {}", id, player.getId(), playerMessage.toString());
                JsonNode messageNode = objectMapper.createObjectNode()
                        .put("channel", playerMessage.getName())
                        .set("data", playerMessage.getData());

                sendMessageToPlayer(player.getId(), messageNode.toString());
            });
        });

        game.registerObserver(actionDetails -> {
            logger.info("RoomId: {}, GameMessage: {}", id, actionDetails.toString());
            JsonNode messageNode = objectMapper.createObjectNode()
                    .put("channel", actionDetails.getName())
                    .set("data", actionDetails.getData());

            sendMessageToAllPlayers(messageNode.toString());
        });
    }

    public void handleMessage(WebSocketSession session, JsonNode data) {
        findPlayerIdBySession(session).ifPresent(playerId -> {
            findPlayerById(playerId).ifPresent(player -> {
                player.action(data.get("channel").asText(), data.get("data"));
            });
        });
    }

    private void sendMessageToPlayer(String playerId, String message) {
        try {
            playerIdWebSocketSessions.get(playerId).sendMessage(new TextMessage(message));
        } catch (IOException e) {
            logger.error("game send message to player error playerId: " + playerId + ", message: " + message, e);
        }
    }

    private void sendMessageToAllPlayers(String message) {
        playerIdWebSocketSessions.values().forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                logger.error("game send message to all error message: " + message, e);
            }
        });
    }

    public void setPlayerSocket(String playerId, WebSocketSession webSocketSession) {
        playerIdWebSocketSessions.put(playerId, webSocketSession);
        if (!running && playerIdWebSocketSessions.size() == gameContainer.getGame().getPlayers().size()) {
            gameContainer.startGame();
            running = true;
        }
    }

    public void removePlayerSocket(WebSocketSession webSocketSession) {
        findPlayerIdBySession(webSocketSession).ifPresent(playerId -> playerIdWebSocketSessions.remove(playerId));
    }

    public boolean containsPlayer(String playerId) {
        for (Player player : gameContainer.getGame().getPlayers()) {
            if (player.getId().equals(playerId)) {
                return true;
            }
        }
        logger.warn("containsPlayer false!");
        return false;
    }

    public boolean containsSession(WebSocketSession webSocketSession) {
        return playerIdWebSocketSessions.values().stream()
                .anyMatch(session -> session == webSocketSession);
    }

    private Optional<String> findPlayerIdBySession(WebSocketSession webSocketSession) {
        return playerIdWebSocketSessions.keySet().stream()
                .filter(playerId -> playerIdWebSocketSessions.get(playerId) == webSocketSession)
                .findFirst();
    }

    private Optional<Player> findPlayerById(String playerId) {
        return gameContainer.getGame().getPlayers().stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst();
    }
}
