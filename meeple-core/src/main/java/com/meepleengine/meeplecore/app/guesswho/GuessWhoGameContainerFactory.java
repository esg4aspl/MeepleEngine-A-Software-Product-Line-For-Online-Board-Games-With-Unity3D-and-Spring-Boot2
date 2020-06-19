package com.meepleengine.meeplecore.app.guesswho;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meepleengine.meeplecore.app.GameContainer;
import com.meepleengine.meeplecore.app.GameContainerFactory;
import com.meepleengine.meeplecore.guesswho.GuessWhoGame;
import com.meepleengine.meeplecore.guesswho.GuessWhoGameFactory;
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

public class GuessWhoGameContainerFactory implements GameContainerFactory {

    private final GuessWhoGameFactory guessWhoGameFactory;

    public GuessWhoGameContainerFactory() {
        this.guessWhoGameFactory = new GuessWhoGameFactory();
    }

    public GameContainer create(String drlFilePath, String assetsFilePath, List<String> playerIds) throws Exception {
        // initialize drools session
        KieSession kieSession = createSession(drlFilePath);

        // read assets
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode assetsNode = objectMapper.readValue(new File(assetsFilePath), ObjectNode.class);

        // TODO: create the game with respect to id, lets say id == dixit
        GuessWhoGame game = guessWhoGameFactory.createGame("id", assetsNode, playerIds);
        insertToSession(kieSession, game);

        return new GameContainer(kieSession, game);
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

    private void insertToSession(KieSession kieSession, GuessWhoGame game) {
        kieSession.insert(game);
        kieSession.insert(game.getTurnState());
        kieSession.insert(game.getTurnColor());

        for (Player player: game.getPlayers()) {
            player.setKieSession(kieSession);
            kieSession.insert(player);
            kieSession.insert(player.getRole());
            kieSession.insert(player.getColor());
            kieSession.insert(player.getPlayerMessage());
        }

        kieSession.insert(game.getBoard());
        for (Grid grid: game.getBoard().getGrids()) {
            kieSession.insert(grid);
            for (Item item: grid.getItemList()) {
                kieSession.insert(item);
            }
        }
    }

}
