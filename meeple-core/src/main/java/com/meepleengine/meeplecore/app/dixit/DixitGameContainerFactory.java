package com.meepleengine.meeplecore.app.dixit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.app.GameContainerFactory;
import com.meepleengine.meeplecore.dixit.DixitGame;
import com.meepleengine.meeplecore.dixit.DixitGameFactory;
import com.meepleengine.meeplecore.model.Grid;
import com.meepleengine.meeplecore.model.Item;
import com.meepleengine.meeplecore.model.Player;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class DixitGameContainerFactory implements GameContainerFactory {

    private final DixitGameFactory dixitGameFactory;

    public DixitGameContainerFactory() {
        this.dixitGameFactory = new DixitGameFactory();
    }

    public GameContainer create(String drlFilePath, String assetsFilePath, List<String> playerIds) throws Exception {
        // initialize drools session
        KieSession kieSession = createSession(drlFilePath);

        // read assets
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode assetsNode = objectMapper.readValue(new File(assetsFilePath), ObjectNode.class);

        // TODO: create the game with respect to id, lets say id == dixit
        DixitGame dixitGame = dixitGameFactory.createGame("id", assetsNode, playerIds);
        insertToSession(kieSession, dixitGame);

        return new GameContainer(kieSession, dixitGame);
    }

    private KieSession createSession(String drlFilePath) throws Exception {
        KieServices kieServices = KieServices.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        FileInputStream fileInputStream = new FileInputStream(drlFilePath);
        kieFileSystem.write("src/main/resources/main.drl", kieServices.getResources().newInputStreamResource(fileInputStream));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
        if(results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException(results.getMessages().toString());
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieSession kieSession = kieContainer.newKieSession();
        return kieSession;
    }

    private void insertToSession(KieSession kieSession, DixitGame dixitGame) {
        kieSession.insert(dixitGame);
        kieSession.insert(dixitGame.getTurnState());

        for (Player player: dixitGame.getPlayers()) {
            player.setKieSession(kieSession);
            kieSession.insert(player);
            kieSession.insert(player.getPlayerMessage());
        }

        // TODO: insert all items recursively
        kieSession.insert(dixitGame.getBoard());
        for (Grid grid: dixitGame.getBoard().getGrids()) {
            kieSession.insert(grid);
            for (Item item: grid.getItemList()) {
                kieSession.insert(item);
            }
        }
    }
}
