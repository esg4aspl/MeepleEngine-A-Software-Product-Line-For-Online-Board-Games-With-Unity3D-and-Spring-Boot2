package com.meepleengine.meeplecore.codenames;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meeplecore.model.Role;
import com.meepleengine.meeplecore.model.*;
import com.meepleengine.meeplecore.util.StreamUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodenamesGameFactory {

    public CodenamesGame createGame(String id, JsonNode assetsNode, List<String> playerIds) throws JsonProcessingException {

        // create players, divide into two teams
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < playerIds.size()/2; j++) {
                Player player = new Player();
                player.setId(playerIds.get(playerIds.size()/2*i+j));
                player.setColor(PlayerColor.values()[i]);
                player.setRole(Role.OTHERS);
                players.add(player);
            }
        }

        // create words cards, 69
        List<JsonNode> wordCardJsonList = findObjectsByName(assetsNode, "WordCard");
        List<Card> words = new ArrayList<>();
        for (int i = 0; i < 69; i++) {
            JsonNode wordCardGridJson = findObjectByParentGuid(assetsNode, wordCardJsonList.get(i).get("guid").asText());

            Grid grid = new Grid(wordCardGridJson.get("guid").asText());
            Card card = new Card(grid);
            card.setId(wordCardJsonList.get(i).get("guid").asText());
            grid.setParent(card);

            words.add(card);
        }

        // create keyCards
        List<JsonNode> keysCardJsonList = findObjectsByName(assetsNode, "KeysCard");
        List<Card> keyCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Card card = new Card(null);
            card.setId(keysCardJsonList.get(i).get("guid").asText());

            keyCards.add(card);
        }

        // create redAgentCards will consists 9 red, 1 for double
        List<JsonNode> redAgentCardJsonList = findObjectsByName(assetsNode, "RedAgentCard");
        List<Card> redAgents = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Card agent = new Card(null);
            agent.setId(redAgentCardJsonList.get(i).get("guid").asText());
            agent.setData("type", "RED");
            redAgents.add(agent);
        }

        // create blueAgentCards will consists 9 blue, 1 for double
        List<JsonNode> blueAgentCardJsonList = findObjectsByName(assetsNode, "BlueAgentCard");
        List<Card> blueAgents = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Card agent = new Card(null);
            agent.setId(blueAgentCardJsonList.get(i).get("guid").asText());
            agent.setData("type", "BLUE");
            blueAgents.add(agent);
        }

        // create innocentCards will consists 8 blue
        List<JsonNode> innocentsCardJsonList = findObjectsByName(assetsNode, "InnocentCard");
        List<Card> innocents = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Card innocent = new Card(null);
            innocent.setId(innocentsCardJsonList.get(i).get("guid").asText());
            innocent.setData("type", "INNOCENT");
            innocents.add(innocent);
        }

        // create assassin card
        JsonNode assassinCardJson = findObjectByName(assetsNode, "AssassinCard");
        Card assassinCard = new Card(null);
        assassinCard.setId(assassinCardJson.get("guid").asText());
        assassinCard.setData("type", "ASSASSIN");

        // create words deck, consists all the words, shuffle them
        JsonNode wordsDeckJson = findObjectByName(assetsNode, "WordsDeck");
        JsonNode wordsDeckGridJson = findObjectByName(assetsNode, "WordsDeckGrid");
        Grid wordsDeckGrid = new Grid(wordsDeckGridJson.get("guid").asText());
        Deck wordsDeck = new Deck(wordsDeckJson.get("guid").asText());
        wordsDeck.setGrid(wordsDeckGrid);
        wordsDeckGrid.setParent(wordsDeck);
//        Collections.shuffle(words);
        wordsDeck.addCards(words);

        // select game cards for the game, draw 25 words
        Deck gameWordCards = new Deck(null); // temporary deck (there is not grid on the table)
        gameWordCards.setGrid(new Grid(null));
        gameWordCards.addCards(wordsDeck.draw(25));

        // create key deck, consists all the keys, shuffle them
        JsonNode keysDeckJson = findObjectByName(assetsNode, "KeysDeck");
        JsonNode keysDeckGridJson = findObjectByName(assetsNode, "KeysDeckGrid");
        Grid keysDeckGrid = new Grid(keysDeckGridJson.get("guid").asText());
        Deck keysDeck = new Deck(keysDeckJson.get("guid").asText());
        keysDeck.setGrid(keysDeckGrid);
        keysDeckGrid.setParent(keysDeck);
//        Collections.shuffle(keyCards);
        keysDeck.addCards(keyCards);

        // assign the key for the game, draw 1 key
        Card keyCard = keysDeck.draw();

        // create redAgents
        JsonNode redAgentsDeckJson = findObjectByName(assetsNode, "RedAgentsDeck");
        JsonNode redAgentsDeckGridJson = findObjectByName(assetsNode, "RedAgentsDeckGrid");
        Grid redAgentsDeckGrid = new Grid(redAgentsDeckGridJson.get("guid").asText());
        Deck redAgentsDeck = new Deck(redAgentsDeckJson.get("guid").asText());
        redAgentsDeck.setGrid(redAgentsDeckGrid);
        redAgentsDeckGrid.setParent(redAgentsDeck);
        redAgentsDeck.addCards(redAgents);

        // create blueAgents
        JsonNode blueAgentsDeckJson = findObjectByName(assetsNode, "BlueAgentsDeck");
        JsonNode blueAgentsDeckGridJson = findObjectByName(assetsNode, "BlueAgentsDeckGrid");
        Grid blueAgentsDeckGrid = new Grid(blueAgentsDeckGridJson.get("guid").asText());
        Deck blueAgentsDeck = new Deck(blueAgentsDeckJson.get("guid").asText());
        blueAgentsDeck.setGrid(blueAgentsDeckGrid);
        blueAgentsDeckGrid.setParent(blueAgentsDeck);
        blueAgentsDeck.addCards(blueAgents);

        // create innocents deck
        JsonNode innocentsDeckJson = findObjectByName(assetsNode, "InnocentsDeck");
        JsonNode innocentsDeckGridJson = findObjectByName(assetsNode, "InnocentsDeckGrid");
        Grid innocentsDeckGrid = new Grid(innocentsDeckGridJson.get("guid").asText());
        Deck innocentsDeck = new Deck(innocentsDeckJson.get("guid").asText());
        innocentsDeck.setGrid(innocentsDeckGrid);
        innocentsDeckGrid.setParent(innocentsDeck);
        innocentsDeck.addCards(innocents);

        // create the board
        JsonNode boardJson = findObjectByName(assetsNode, "Table");
        Board board = new Board(boardJson.get("guid").asText());

        // create wordsDeck grid, 0th
        JsonNode wordsDeckContainerGridJson = findObjectByName(assetsNode, "WordsDeckContainerGrid");
        Grid wordsDeckContainerGrid = new Grid(wordsDeckContainerGridJson.get("guid").asText());
        wordsDeckContainerGrid.addItem(wordsDeck);
        wordsDeckContainerGrid.setParent(board);
        board.addGrid(wordsDeckContainerGrid);

        // create keysDeck grid, 1st
        JsonNode keysDeckContainerGridJson = findObjectByName(assetsNode, "KeysDeckContainerGrid");
        Grid keysDeckContainerGrid = new Grid(keysDeckContainerGridJson.get("guid").asText());
        keysDeckContainerGrid.addItem(keysDeck);
        keysDeckContainerGrid.setParent(board);
        board.addGrid(keysDeckContainerGrid);

        // create word board grid, 2nd
        JsonNode wordBoardJson = findObjectByName(assetsNode, "WordBoard");
        Board wordBoard = new Board(wordBoardJson.get("guid").asText());
        List<JsonNode> wordBoardGridJsonList = findObjectsByName(assetsNode, "WordBoardGrid");
        for (int i = 0; i < gameWordCards.getCards().size(); i++) {
            Item card = gameWordCards.getCards().get(i);
            Grid wordBoardGrid = new Grid(wordBoardGridJsonList.get(i).get("guid").asText());
            wordBoardGrid.addItem(card);
            wordBoardGrid.setParent(wordBoard);
            wordBoard.addGrid(wordBoardGrid);
        }
        JsonNode wordBoardContainerGridJson = findObjectByName(assetsNode, "WordsBoardContainerGrid");
        Grid wordBoardContainerGrid = new Grid(wordBoardContainerGridJson.get("guid").asText());
        wordBoardContainerGrid.addItem(wordBoard);
        wordBoardContainerGrid.setParent(board);
        board.addGrid(wordBoardContainerGrid);

        // create keyCard grid, 3rd
        JsonNode keyCardContainerGridJson = findObjectByName(assetsNode, "KeyCardContainerGrid");
        Grid keyCardContainerGrid = new Grid(keyCardContainerGridJson.get("guid").asText());
        keyCardContainerGrid.addItem(keyCard);
        keyCardContainerGrid.setParent(board);
        board.addGrid(keyCardContainerGrid);

        // create redAgentsDeck grid, 4th
        JsonNode redAgentsDeckContainerGridJson = findObjectByName(assetsNode, "RedAgentsDeckContainerGrid");
        Grid redAgentsDeckContainerGrid = new Grid(redAgentsDeckContainerGridJson.get("guid").asText());
        redAgentsDeckContainerGrid.addItem(redAgentsDeck);
        redAgentsDeckContainerGrid.setParent(board);
        board.addGrid(redAgentsDeckContainerGrid);

        // create blueAgentsDeck grid, 5th
        JsonNode blueAgentsDeckContainerGridJson = findObjectByName(assetsNode, "BlueAgentsDeckContainerGrid");
        Grid blueAgentsDeckContainerGrid = new Grid(blueAgentsDeckContainerGridJson.get("guid").asText());
        blueAgentsDeckContainerGrid.addItem(blueAgentsDeck);
        blueAgentsDeckContainerGrid.setParent(board);
        board.addGrid(blueAgentsDeckContainerGrid);

        // create innocentsDeck grid, 6th
        JsonNode innocentsDeckContainerGridJson = findObjectByName(assetsNode, "InnocentsDeckContainerGrid");
        Grid innocentsDeckContainerGrid = new Grid(innocentsDeckContainerGridJson.get("guid").asText());
        innocentsDeckContainerGrid.addItem(innocentsDeck);
        innocentsDeckContainerGrid.setParent(board);
        board.addGrid(innocentsDeckContainerGrid);

        // create assassin grid, 7th
        JsonNode assassinCardContainerGridJson = findObjectByName(assetsNode, "AssassinCardContainerGrid");
        Grid assassinCardContainerGrid = new Grid(assassinCardContainerGridJson.get("guid").asText());
        assassinCardContainerGrid.addItem(assassinCard);
        assassinCardContainerGrid.setParent(board);
        board.addGrid(assassinCardContainerGrid);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode keyCardData = objectMapper.readTree(findObjectByGuid(assetsNode, keyCard.getId()).get("data").get("layout").asText());
        List<CardType> cardTypes = StreamUtils.asStream(keyCardData.elements()).map(keyCardNode -> CardType.getCardType(keyCardNode.asInt())).collect(Collectors.toList());

        // key card and map with word and teams
        Map<Card, CardType> keyMap = new HashMap<>();
        for (int i = 0; i < gameWordCards.getCards().size(); i++) {
            keyMap.put((Card) gameWordCards.getCards().get(i), cardTypes.get(i));
        }

        // beginning state
        TurnState turnState = TurnState.READY;

        return new CodenamesGame(id, players, turnState, board, wordsDeck, gameWordCards, keyMap, blueAgentsDeck, redAgentsDeck);
    }

    private JsonNode findObjectByName(JsonNode assetsNode, String name) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> name.equals(object.get("name").asText())).findFirst().get();
    }

    private List<JsonNode> findObjectsByName(JsonNode assetsNode, String name) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> name.equals(object.get("name").asText())).collect(Collectors.toList());
    }

    private JsonNode findObjectByGuid(JsonNode assetsNode, String guid) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> guid.equals(object.get("guid").asText())).findFirst().get();
    }

    private JsonNode findObjectByParentGuid(JsonNode assetsNode, String parentGuid) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> object.has("parentGuid") && parentGuid.equals(object.get("parentGuid").asText())).findFirst().get();
    }

}
