package com.meepleengine.meepleserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.app.GameContainerFactory;
import com.meepleengine.meeplecore.app.codenames.CodenamesGameContainerFactory;
import com.meepleengine.meeplecore.app.dixit.DixitGameContainerFactory;
import com.meepleengine.meeplecore.app.guesswho.GuessWhoGameContainerFactory;
import com.meepleengine.meepleserver.model.Lobby;
import com.meepleengine.meepleserver.service.socket.Room;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final List<Room> rooms = new ArrayList<>();

    private final Map<String, GameContainerFactory> gameFactoryMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameService() {
        gameFactoryMap.put("GuessWho", new GuessWhoGameContainerFactory());
        gameFactoryMap.put("Codenames", new CodenamesGameContainerFactory());
        gameFactoryMap.put("Dixit", new DixitGameContainerFactory());

    }

    public void createGame(Lobby lobby) {
        String roomId = lobby.getId().toHexString();
        List<String> userHexIds = lobby.getUsers().stream().map(ObjectId::toHexString).collect(Collectors.toList());
        final String drlFilePath = "src/main/resources/rules/" + lobby.getGameName() + ".drl";
        final String assetsFilePath = "src/main/resources/assets/" + lobby.getGameName() + ".json";

        GameContainer gameContainer;
        try {
            gameContainer = gameFactoryMap.get(lobby.getGameName()).create(drlFilePath, assetsFilePath, userHexIds);
        } catch (Exception e) {
            logger.error("Initialize game error:", e);
            return;
        }

        Room room = new Room(roomId, gameContainer);
        room.registerObservers();
        rooms.add(room);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        logger.info("Connected: {}", webSocketSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        findRoomBySession(webSocketSession).ifPresent(room -> room.removePlayerSocket(webSocketSession));
        logger.info("Disconnected: {}", webSocketSession.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);
        logger.info("Handle Message: {}", jsonNode.toPrettyString());

        switch (jsonNode.get("channel").asText()) {
            case "Player":
                handlePlayerMessage(session, jsonNode.get("data"));
                break;
            case "Click":
            case "Select":
            case "Move":
            case "Flip":
                handleMessage(session, jsonNode);
                break;
        }
    }

    private void handlePlayerMessage(WebSocketSession session, JsonNode data) {
        String playerId = data.get("playerId").asText();
        findRoomByPlayerId(playerId).ifPresent(room -> room.setPlayerSocket(playerId, session));
    }

    private void handleMessage(WebSocketSession session, JsonNode data) {
        findRoomBySession(session).ifPresent(room -> room.handleMessage(session, data));
    }

    private Optional<Room> findRoomByPlayerId(String playerId) {
        for (Room room : rooms) {
            if(room.containsPlayer(playerId)) {
                return Optional.of(room);
            }
        }
        logger.warn("findRoomByPlayerId not found!");
        return Optional.empty();
    }

    private Optional<Room> findRoomBySession(WebSocketSession session) {
        for (Room room : rooms) {
            if(room.containsSession(session)) {
                return Optional.of(room);
            }
        }
        logger.warn("findRoomBySession not found!");
        return Optional.empty();
    }
}
