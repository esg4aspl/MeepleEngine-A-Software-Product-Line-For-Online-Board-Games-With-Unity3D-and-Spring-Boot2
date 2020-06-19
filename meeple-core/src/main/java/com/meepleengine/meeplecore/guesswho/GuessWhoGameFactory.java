package com.meepleengine.meeplecore.guesswho;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.meepleengine.meeplecore.codenames.CardType;
import com.meepleengine.meeplecore.model.*;
import com.meepleengine.meeplecore.util.StreamUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GuessWhoGameFactory {

    public GuessWhoGame createGame(String id, JsonNode assetsNode, List<String> playerIds) {

        // create players, divide into two teams
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Player player = new Player();
            player.setId(playerIds.get(i));
            player.setColor(PlayerColor.values()[i]);
            player.setRole(Role.OTHERS);
            players.add(player);
        }

        // create character cards, 24
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());

        // 60 cards, first 20 is for deck, second 20 is for red, third 20 is for blue cards
        List<JsonNode> charactersJsonNodeArray = objectsStream.filter(object -> "card".equals(object.get("type").asText())).collect(Collectors.toList());
        List<Card> characters = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Card card = new Card(null);
            card.setId(charactersJsonNodeArray.get(i).get("guid").asText());
            card.setData("id", charactersJsonNodeArray.get(i).get("data").get("id").asText());

            characters.add(card);
        }

        List<Item> redCharacters = new ArrayList<>();
        for (int i = 20; i < 40; i++) {
            Card card = new Card(null);
            card.setId(charactersJsonNodeArray.get(i).get("guid").asText());
            card.setData("id", charactersJsonNodeArray.get(i).get("data").get("id").asText());

            redCharacters.add(card);
        }

        List<Item> blueCharacters = new ArrayList<>();
        for (int i = 40; i < 60; i++) {
            Card card = new Card(null);
            card.setId(charactersJsonNodeArray.get(i).get("guid").asText());
            card.setData("id", charactersJsonNodeArray.get(i).get("data").get("id").asText());

            blueCharacters.add(card);
        }

        // create character deck, consists all the characters, shuffle them
//        Collections.shuffle(characters);

        JsonNode deckJson = findObjectByName(assetsNode, "Deck");
        JsonNode deckGridJson = findObjectByName(assetsNode, "DeckGrid");

        // create deck and deck grid
        Grid mysteryDeckGrid = new Grid(deckGridJson.get("guid").asText());
        Deck mysteryDeck = new Deck(deckJson.get("guid").asText());
        mysteryDeck.setGrid(mysteryDeckGrid);
        mysteryDeckGrid.setParent(mysteryDeck);

        // add cards
        mysteryDeck.addCards(characters);

        // create deck container grid
        JsonNode deckContainerGridJson = findObjectByName(assetsNode, "DeckContainerGrid");
        Grid mysteryDeckContainerGrid = new Grid(deckContainerGridJson.get("guid").asText());
        mysteryDeckContainerGrid.addItem(mysteryDeck);


        // Create Mystery Card Grids
        JsonNode redMysteryCardGrid = findObjectByName(assetsNode, "RedCardGrid");
        JsonNode blueMysteryCardGrid = findObjectByName(assetsNode, "BlueCardGrid");

        Card redMysteryCard = mysteryDeck.draw();
        Card blueMysteryCard = mysteryDeck.draw(); // todo: send mystery deck's cards id order

        Grid redMysteryGrid = new Grid(redMysteryCardGrid.get("guid").asText());
        redMysteryGrid.addItem(redMysteryCard);

        Grid blueMysteryGrid = new Grid(blueMysteryCardGrid.get("guid").asText());
        blueMysteryGrid.addItem(blueMysteryCard);


        // === Create Player Boards ===

        // create board for red, 0th
        JsonNode redBoardJson = findObjectByName(assetsNode, "RedBoard");
        Board redBoard = new Board(redBoardJson.get("guid").asText());
        for (Item item: redCharacters) {
            JsonNode redCardJson = findObjectByGuid(assetsNode, item.getId());
            Grid redGrid = new Grid(redCardJson.get("currentGridGuid").asText());
            redGrid.addItem(item);
            redGrid.setParent(redBoard);
            redBoard.addGrid(redGrid);
        }

        JsonNode redBoardContainerGridJson = findObjectByName(assetsNode, "RedBoardContainerGrid");
        Grid redBoardContainerGrid = new Grid(redBoardContainerGridJson.get("guid").asText());
        redBoardContainerGrid.addItem(redBoard);

        // create board for red, 0th
        JsonNode blueBoardJson = findObjectByName(assetsNode, "BlueBoard");
        Board blueBoard = new Board(blueBoardJson.get("guid").asText());
        for (Item item: blueCharacters) {
            JsonNode blueCardJson = findObjectByGuid(assetsNode, item.getId());
            Grid blueGrid = new Grid(blueCardJson.get("currentGridGuid").asText());
            blueGrid.addItem(item);
            blueGrid.setParent(blueBoard);
            blueBoard.addGrid(blueGrid);
        }

        JsonNode blueBoardContainerGridJson = findObjectByName(assetsNode, "BlueBoardContainerGrid");
        Grid blueBoardContainerGrid = new Grid(blueBoardContainerGridJson.get("guid").asText());
        blueBoardContainerGrid.addItem(blueBoard);


        // === create the board ===
        JsonNode boardJson = findObjectByName(assetsNode, "Table");
        Board board = new Board(boardJson.get("guid").asText());

        // board for red, 0th
        redBoardContainerGrid.setParent(board);
        board.addGrid(redBoardContainerGrid);

        // board for blue, 1st
        blueBoardContainerGrid.setParent(board);
        board.addGrid(blueBoardContainerGrid);

        // mysteryDeck grid, 2nd
        mysteryDeckContainerGrid.setParent(board);
        board.addGrid(mysteryDeckContainerGrid);

        // redMysteryCard grid, 3rd
        redMysteryGrid.setParent(board);
        board.addGrid(redMysteryGrid);

        // blueMysteryCard grid, 4th
        blueMysteryGrid.setParent(board);
        board.addGrid(blueMysteryGrid);

        // redGuess grid, 5th
        JsonNode redGuessGridJson = findObjectByName(assetsNode, "RedGuessGrid");
        Grid redGuessGrid = new Grid(redGuessGridJson.get("guid").asText());
        redGuessGrid.setParent(board);
        board.addGrid(redGuessGrid);

        // blueGuess grid, 6th
        JsonNode blueGuessGridJson = findObjectByName(assetsNode, "BlueGuessGrid");
        Grid blueGuessGrid = new Grid(blueGuessGridJson.get("guid").asText());
        blueGuessGrid.setParent(board);
        board.addGrid(blueGuessGrid);

        // beginning state
        TurnState turnState = TurnState.READY;

        return new GuessWhoGame(id, players, turnState, board, mysteryDeck, redMysteryCard, blueMysteryCard);
    }

    private JsonNode findObjectByName(JsonNode assetsNode, String name) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> name.equals(object.get("name").asText())).findFirst().get();
    }

    private JsonNode findObjectByGuid(JsonNode assetsNode, String guid) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> guid.equals(object.get("guid").asText())).findFirst().get();
    }

}
