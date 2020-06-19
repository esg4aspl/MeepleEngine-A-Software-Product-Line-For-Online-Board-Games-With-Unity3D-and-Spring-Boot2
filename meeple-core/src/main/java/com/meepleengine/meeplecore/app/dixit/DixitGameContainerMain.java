package com.meepleengine.meeplecore.app.dixit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.model.Game;
import com.meepleengine.meeplecore.model.Player;

import java.util.ArrayList;
import java.util.List;

public class DixitGameContainerMain {

    public static void main(String[] args) throws Exception {

        DixitGameContainerFactory dixitGameContainerFactory = new DixitGameContainerFactory();

        final String drlFilePath = "/Users/elifduran/ceng/meeple/meeple-core/src/main/resources/rules/Dixit.drl";
        final String assetsFilePath = "/Users/elifduran/ceng/meeple/meeple-core/src/main/resources/assets/Dixit.json";
        List<String> ids = new ArrayList<>();
        ids.add("elo1");
        ids.add("beko1");
        ids.add("sems1");
        ids.add("elo2");

        GameContainer gameContainer = dixitGameContainerFactory.create(drlFilePath, assetsFilePath, ids);

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

        node = mapper.createObjectNode().put("objectId", "229922");
        elo1.action("Select", node);
        Thread.sleep(1000);

        Player beko1 = playerList.get(1);
        node = mapper.createObjectNode().put("objectId", "231508");
        beko1.action("Select", node);
        Thread.sleep(1000);

        Player sems1 = playerList.get(2);
        node = mapper.createObjectNode().put("objectId", "228768");
        sems1.action("Select", node);
        Thread.sleep(1000);

        Player elo2 = playerList.get(3);
        node = mapper.createObjectNode().put("objectId", "228208");
        elo2.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "232044");
        beko1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "227570");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "231606");
        elo2.action("Select", node);
        Thread.sleep(1000);

        // new turn
        node = mapper.createObjectNode().put("objectId", "229528");
        beko1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "230234");
        elo2.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "231822");
        elo1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "229848"); //231456
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "227570");
        sems1.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "231606");
        elo2.action("Select", node);
        Thread.sleep(1000);

        node = mapper.createObjectNode().put("objectId", "230630");
        elo1.action("Select", node);
        Thread.sleep(1000);

//
//        node = mapper.createObjectNode().put("objectId", "RED_0");
//        elo1.action("Move", node);
//        Thread.sleep(1000);
//
//        node = mapper.createObjectNode().put("objectId", "GREEN_1");
//        sems1.action("Move", node);
//        Thread.sleep(1000);
//
//        node = mapper.createObjectNode().put("objectId", "YELLOW_2");
//        elo2.action("Move", node);
//        Thread.sleep(1000);
//
//        node = mapper.createObjectNode().put("objectId", "BLACK_3");
//        beko2.action("Move", node);
//        Thread.sleep(1000);
//
//        node = mapper.createObjectNode().put("objectId", "WHITE_4");
//        sems2.action("Move", node);
    }

    private static void println(String msg) {
        System.out.println("main: " + msg);
    }
}
