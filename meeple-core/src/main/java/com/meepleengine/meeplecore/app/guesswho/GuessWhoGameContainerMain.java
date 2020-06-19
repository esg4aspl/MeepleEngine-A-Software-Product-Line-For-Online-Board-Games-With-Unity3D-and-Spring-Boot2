package com.meepleengine.meeplecore.app.guesswho;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.model.Board;
import com.meepleengine.meeplecore.model.Card;
import com.meepleengine.meeplecore.model.Game;
import com.meepleengine.meeplecore.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GuessWhoGameContainerMain {

    public static void main(String[] args) throws Exception {

        GuessWhoGameContainerFactory guessWhoGameContainerFactory = new GuessWhoGameContainerFactory();

        final String drlFilePath = "/Users/elifduran/ceng/meeple/meeple-core/src/main/resources/rules/GuessWho.drl";
        final String assetsFilePath = "/Users/elifduran/ceng/meeple/meeple-core/src/main/resources/assets/GuessWho.json";
        List<String> ids = new ArrayList<>();
        ids.add("elo1");
        ids.add("beko1");

        GameContainer gameContainer = guessWhoGameContainerFactory.create(drlFilePath, assetsFilePath, ids);

        Game game = gameContainer.getGame();
        game.registerObserver((actionDetails) -> {
            println(actionDetails.toString());
        });

        println("start game");
        gameContainer.startGame();

        List<Player> playerList = game.getPlayers();
        ObjectMapper mapper = new ObjectMapper();

        for (Player player: playerList) {
            player.registerObserver((actionDetails) -> {
                println(actionDetails.toString());
            });
        }

        Player elo1 = playerList.get(0);
        JsonNode node = mapper.createObjectNode().put("buttonName", "Ready");
        elo1.action("Click", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-1558");
        elo1.action("Flip", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-1608");
        elo1.action("Flip", node);
        Thread.sleep(1000);

//        node = mapper.createObjectNode().put("objectId", "21");
//        elo1.action("Move", node);
//        Thread.sleep(1000);

        node = mapper.createObjectNode().put("buttonName", "Done");
        elo1.action("Click", node);
        Thread.sleep(1000);

        Player beko1 = playerList.get(1);
        node = mapper.createObjectNode().put("objectId", "49");
        beko1.action("Flip", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "57");
        beko1.action("Flip", node);
        Thread.sleep(1000);

        String redMysteryCardData = ((Card) gameContainer.getGame().getBoard().getGrids().get(3).getItemList().get(0)).getData().get("id").asText();

        Board blueBoard = (Board) gameContainer.getGame().getBoard().getGrids().get(1).getItemList().get(0);
        String blueGuessCardId = blueBoard.getGrids().get(Integer.parseInt(redMysteryCardData)).getItemList().get(0).getId();

        node = mapper.createObjectNode().put("objectId", blueGuessCardId);
        beko1.action("Move", node);
        Thread.sleep(1000);
    }

    private static void println(String msg) {
        System.out.println("main: " + msg);
    }
}
