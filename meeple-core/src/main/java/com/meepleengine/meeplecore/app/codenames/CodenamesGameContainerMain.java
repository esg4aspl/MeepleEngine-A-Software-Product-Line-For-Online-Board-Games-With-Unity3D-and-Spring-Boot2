package com.meepleengine.meeplecore.app.codenames;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.model.Game;
import com.meepleengine.meeplecore.model.Player;

import java.util.ArrayList;
import java.util.List;

public class CodenamesGameContainerMain {

    public static void main(String[] args) throws Exception {

        CodenamesGameContainerFactory codenamesGameContainerFactory = new CodenamesGameContainerFactory();

        final String drlFilePath = "/Users/elifduran/ceng/meeple/meeple-core/src/main/resources/rules/Codenames.drl";
        final String assetsFilePath = "/Users/elifduran/ceng/meeple/meeple-core/src/main/resources/assets/Codenames.json";
        List<String> ids = new ArrayList<>();
        ids.add("elo1");
        ids.add("beko1");
        ids.add("sems1");
        ids.add("elo2");
        ids.add("beko2");
        ids.add("sems2");

        GameContainer gameContainer = codenamesGameContainerFactory.create(drlFilePath, assetsFilePath, ids);

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
        JsonNode node = mapper.createObjectNode().put("buttonName", "Spymaster");
        elo1.action("Click", node);

        Player beko2 = playerList.get(4);
        node = mapper.createObjectNode().put("buttonName", "Spymaster");
        beko2.action("Click", node);
        Thread.sleep(1000);

        // whisper
        node = mapper.createObjectNode().put("buttonName", "9");
        elo1.action("Click", node);
        Thread.sleep(1000);

        Player beko1 = playerList.get(1);
        node = mapper.createObjectNode().put("objectId", "-115990");  // -25374 assassin
        beko1.action("Select", node);
        Thread.sleep(1000);

        Player sems1 = playerList.get(2);
        node = mapper.createObjectNode().put("objectId", "-120600");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-121638");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-116410");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-115792");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-121020");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-118082");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-118280");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("buttonName", "Stop");
        sems1.action("Click", node);
        Thread.sleep(1000);

        // whisper
        node = mapper.createObjectNode().put("buttonName", "0");
        beko2.action("Click", node);
        Thread.sleep(1000);

        Player elo2 = playerList.get(3);
        node = mapper.createObjectNode().put("objectId", "-121440");
        elo2.action("Select", node);
        Thread.sleep(1000);

        // whisper
        node = mapper.createObjectNode().put("buttonName", "1");
        elo1.action("Click", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-119330"); // innocent
        sems1.action("Select", node);
        Thread.sleep(1000);

        // whisper
        node = mapper.createObjectNode().put("buttonName", "1");
        beko2.action("Click", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "-119750"); // ends game
        sems1.action("Select", node); // not your turn
        Thread.sleep(1000);

        elo2.action("Select", node);
    }

    private static void println(String msg) {
        System.out.println("main: " + msg);
    }
}
