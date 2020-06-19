package com.meepleengine.meeplecore.dixit;

import com.fasterxml.jackson.databind.JsonNode;
import com.meepleengine.meeplecore.model.*;
import com.meepleengine.meeplecore.util.StreamUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DixitGameFactory {

    public DixitGame createGame(String id, JsonNode assetsNode, List<String> playerIds) {

        // ==== BOARD ====
        JsonNode boardJson = findObjectByName(assetsNode, "Table");
        Board board = new Board(boardJson.get("guid").asText());

        // create players
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerIds.size(); i++) {
            Player player = new Player();
            player.setId(playerIds.get(i));
            player.setColor(PlayerColor.values()[i]);
            player.setScore(0);
            player.setRole(Role.OTHERS);

            players.add(player);
        }

        // create cards, set 36 to test end condition
        List<JsonNode> cardJsonList = findObjectsByName(assetsNode, "Card");
        List<Card> allCards = new ArrayList<>();
        for (int i = 0; i < 69; i++) {
            Card card = new Card(null);
            card.setId(cardJsonList.get(i).get("guid").asText());

            allCards.add(card);
        }

        // ==== DRAW PILE ====
        JsonNode drawPileJson = findObjectByName(assetsNode, "DrawPile");
        JsonNode drawPileGridJson = findObjectByName(assetsNode, "DrawPileGrid");

        // create a draw pile for remaining cards and grid
        Grid drawPileGrid = new Grid(drawPileGridJson.get("guid").asText());
        Deck drawPile = new Deck(drawPileJson.get("guid").asText());
        drawPile.setGrid(drawPileGrid);
        drawPileGrid.setParent(drawPile);

        // add cards
        drawPile.addCards(allCards);

        // create draw pile container grid, 0th grid
        JsonNode drawPileContainerGridJson = findObjectByName(assetsNode, "DrawPileContainerGrid");
        Grid drawPileContainerGrid = new Grid(drawPileContainerGridJson.get("guid").asText());
        drawPileContainerGrid.addItem(drawPile);

        drawPileContainerGrid.setParent(board);
        board.addGrid(drawPileContainerGrid);

        // ==== SHUFFLE PILE ====
        JsonNode shufflePileJson = findObjectByName(assetsNode, "ShufflePile");
        JsonNode shufflePileGridJson = findObjectByName(assetsNode, "ShufflePileGrid");

        // create a shuffle pile
        Grid shufflePileGrid = new Grid(shufflePileGridJson.get("guid").asText());
        Deck shufflePile = new Deck(shufflePileJson.get("guid").asText());
        shufflePile.setGrid(shufflePileGrid);
        shufflePileGrid.setParent(shufflePile);

        // create shuffle pile container grid, 1st grid
        JsonNode shufflePileContainerGridJson = findObjectByName(assetsNode, "ShufflePileContainerGrid");
        Grid shufflePileContainerGrid = new Grid(shufflePileContainerGridJson.get("guid").asText());
        shufflePileContainerGrid.addItem(shufflePile);

        shufflePileContainerGrid.setParent(board);
        board.addGrid(shufflePileContainerGrid);

        // ==== DISCARD PILE ====
        JsonNode discardPileJson = findObjectByName(assetsNode, "DiscardPile");
        JsonNode discardPileGridJson = findObjectByName(assetsNode, "DiscardPileGrid");

        // create a discard pile
        Grid discardPileGrid = new Grid(discardPileGridJson.get("guid").asText());
        Deck discardPile = new Deck(discardPileJson.get("guid").asText());
        discardPile.setGrid(discardPileGrid);
        discardPileGrid.setParent(discardPile);

        // create discard pile container grid, 2nd grid
        JsonNode discardPileContainerGridJson = findObjectByName(assetsNode, "DiscardPileContainerGrid");
        Grid discardPileContainerGrid = new Grid(discardPileContainerGridJson.get("guid").asText());
        discardPileContainerGrid.addItem(discardPile);

        discardPileContainerGrid.setParent(board);
        board.addGrid(discardPileContainerGrid);

        // ==== VOTING TOKENS ====
        for (Player player: players) {
            Map<String, PlayerItem> playerItemMap = new HashMap<>();

            JsonNode playerTokenAreaContainerGridJson = findObjectByName(assetsNode, toCamelCase(player.getColor().toString()) + "PlayerTokenAreaContainerGrid");
            JsonNode playerTokenAreaJson = findObjectByName(assetsNode, toCamelCase(player.getColor().toString()) + "PlayerTokenArea");

            // create player token area container grid, 3,4,5,6th grid
            Grid playerTokenAreaContainerGrid = new Grid(playerTokenAreaContainerGridJson.get("guid").asText());
            Board playerTokenArea = new Board(playerTokenAreaJson.get("guid").asText());
            playerTokenAreaContainerGrid.addItem(playerTokenArea);

            board.addGrid(playerTokenAreaContainerGrid);
            playerTokenAreaContainerGrid.setParent(board);

            List<JsonNode> playerTokenAreaGridJson = findObjectsByName(assetsNode, toCamelCase(player.getColor().toString()) + "PlayerTokenAreaGrid");
            List<JsonNode> votingTokenJson = findObjectsByName(assetsNode, toCamelCase(player.getColor().toString()) + "VotingToken");

            for (int i = 0; i < 6; i++) {
                PlayerItem votingToken = new PlayerItem(votingTokenJson.get(i).get("guid").asText());
                votingToken.setPlayer(player);
                votingToken.setNo(votingTokenJson.get(i).get("data").get("number").asInt());

                Grid playerTokenAreaGrid = new Grid(playerTokenAreaGridJson.get(i).get("guid").asText());
                playerTokenAreaGrid.addItem(votingToken);
                playerTokenAreaGrid.setParent(playerTokenArea);
                playerTokenArea.addGrid(playerTokenAreaGrid);

                playerItemMap.put("token" + votingToken.getNo(), votingToken);
            }
            player.setPlayerItemMap(playerItemMap);
        }

        // shuffle all cards
//        Collections.shuffle(drawPile.getCards()); // TODO: uncomment here

        // ==== PLAYER HAND ====
        // give 6 cards to players as playerHands
        for (Player player: players) {
            JsonNode playerHandJson = findObjectByName(assetsNode, toCamelCase(player.getColor().toString()) + "Hand");
            JsonNode playerHandGridJson = findObjectByName(assetsNode, toCamelCase(player.getColor().toString()) + "HandGrid");
            JsonNode playerHandContainerGridJson = findObjectByName(assetsNode, toCamelCase(player.getColor().toString()) + "HandContainerGrid");

            PlayerHand playerHand = new PlayerHand(playerHandJson.get("guid").asText());
            player.setPlayerHand(playerHand);

            Grid playerHandGrid = new Grid(playerHandGridJson.get("guid").asText());
            playerHand.setGrid(playerHandGrid);
            playerHandGrid.setParent(playerHand);

            // create player hand container grid, 7,8,9,10 grid
            Grid playerHandContainerGrid = new Grid(playerHandContainerGridJson.get("guid").asText());
            playerHandContainerGrid.addItem(playerHand);

            board.addGrid(playerHandContainerGrid);
            playerHandContainerGrid.setParent(board);

            playerHand.addCards(drawPile.draw(6));
        }

        // ==== TOKEN BOARD ====
        JsonNode tokenBoardContainerJson = findObjectByName(assetsNode, "TokenBoardContainerGrid");
        JsonNode tokenBoardJson = findObjectByName(assetsNode, "TokenBoard");
        List<JsonNode> tokenBoardGridJson = findObjectsByName(assetsNode, "TokenBoardGrid");

        // create token board container grid, 11th grid
        Grid tokenBoardContainerGrid = new Grid(tokenBoardContainerJson.get("guid").asText());
        Board tokenBoard = new Board(tokenBoardJson.get("guid").asText());
        tokenBoardContainerGrid.addItem(tokenBoard);

        tokenBoardContainerGrid.setParent(board);
        board.addGrid(tokenBoardContainerGrid);

        for (int i = 0; i < tokenBoardGridJson.size(); i++) {
            Grid tokenBoardGrid = new Grid(tokenBoardGridJson.get(i).get("guid").asText());
            tokenBoardGrid.setParent(tokenBoard);
            tokenBoard.addGrid(tokenBoardGrid);
        }

        // ==== CARD REVEAL BOARD ====
        JsonNode cardRevealBoardContainerJson = findObjectByName(assetsNode, "CardRevealBoardContainerGrid");
        JsonNode cardRevealBoardJson = findObjectByName(assetsNode, "CardRevealBoard");
        List<JsonNode> cardRevealBoardGridJson = findObjectsByName(assetsNode, "CardRevealBoardGrid");

        // create card reveal board container grid, 12th grid
        Grid cardRevealBoardContainerGrid = new Grid(cardRevealBoardContainerJson.get("guid").asText());
        Board cardRevealBoard = new Board(cardRevealBoardJson.get("guid").asText());
        cardRevealBoardContainerGrid.addItem(cardRevealBoard);

        cardRevealBoardContainerGrid.setParent(board);
        board.addGrid(cardRevealBoardContainerGrid);

        for (int i = 0; i < cardRevealBoardGridJson.size(); i++) {
            Grid cardRevealBoardGrid = new Grid(cardRevealBoardGridJson.get(i).get("guid").asText());
            cardRevealBoardGrid.setParent(cardRevealBoard);
            cardRevealBoard.addGrid(cardRevealBoardGrid);
        }

        // ==== SCOREBOARD ====
        // create scoreboard 31 grids
        JsonNode scoreboardContainerGridJson = findObjectByName(assetsNode, "ScoreBoardContainerGrid");
        JsonNode scoreboardJson = findObjectByName(assetsNode, "ScoreBoard");

        List <JsonNode> rabbitBoardJsonList = findObjectsByName(assetsNode, "RabbitBoard");
        List <JsonNode> rabbitBoardGridJsonList = findObjectsByName(assetsNode, "RabbitBoardGrid");

        // create scoreboard container grid, 13th grid
        Board scoreboard = new Board(scoreboardJson.get("guid").asText());
        Grid scoreboardContainerGrid = new Grid(scoreboardContainerGridJson.get("guid").asText());
        scoreboardContainerGrid.addItem(scoreboard);

        scoreboardContainerGrid.setParent(board);
        board.addGrid(scoreboardContainerGrid);

        for (int i = 0; i < 31; i++) {
            JsonNode scoreboardGridJson = findObjectByName(assetsNode, "ScoreBoardGrid" + i);
            Grid scoreboardGrid = new Grid(scoreboardGridJson.get("guid").asText());
            scoreboardGrid.setParent(scoreboard);
            scoreboard.addGrid(scoreboardGrid);

            Board rabbitBoard = new Board(rabbitBoardJsonList.get(i).get("guid").asText());
            scoreboardGrid.addItem(rabbitBoard);
            for (int j = 0; j < 6; j++) {
                Grid rabbitBoardGrid = new Grid(rabbitBoardGridJsonList.get(i*6+j).get("guid").asText());
                rabbitBoard.addGrid(rabbitBoardGrid);
                rabbitBoardGrid.setParent(rabbitBoard);
            }
        }

        // ==== RABBITS ====
        // create 6 rabbits for score tracking
        Board rabbitBoard = (Board) scoreboard.getGrids().get(0).getItemList().get(0);
        for (int i = 0; i < players.size(); i++) {
            JsonNode rabbitJson = findObjectByName(assetsNode, toCamelCase(players.get(i).getColor().toString()) + "Rabbit");
            PlayerItem rabbit = new PlayerItem(rabbitJson.get("guid").asText());

            Grid rabbitGrid = rabbitBoard.getGrids().get(i);
            rabbitGrid.addItem(rabbit);

            rabbit.setPlayer(players.get(i));
            players.get(i).getPlayerItemMap().put("rabbit", rabbit);
        }

        // beginning state
        TurnState turnState = TurnState.READY;

        return new DixitGame(id, players, turnState, board, drawPile, shufflePile, discardPile);
    }

    private JsonNode findObjectByName(JsonNode assetsNode, String name) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> name.equals(object.get("name").asText())).findFirst().get();
    }

    private List<JsonNode> findObjectsByName(JsonNode assetsNode, String name) {
        Stream<JsonNode> objectsStream = StreamUtils.asStream(assetsNode.get("objects").elements());
        return objectsStream.filter(object -> name.equals(object.get("name").asText())).collect(Collectors.toList());
    }

    private String toCamelCase(String word) {
        final StringBuilder ret = new StringBuilder(word.length());

        for (String w: word.split(" ")) {
            if (!w.isEmpty()) {
                ret.append(Character.toUpperCase(w.charAt(0)));
                ret.append(w.substring(1).toLowerCase());
            }
            if (!(ret.length() == word.length()))
                ret.append(" ");
        }

        return ret.toString();
    }
}
