package com.meepleengine.meeplecore.app;

import com.meepleengine.meeplecore.model.Game;
import org.kie.api.runtime.KieSession;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameContainer {

    private KieSession kieSession;
    private Game game;

    public GameContainer(KieSession kieSession, Game game) {
        this.kieSession = kieSession;
        this.game = game;
    }

    public void startGame() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            try {
                kieSession.fireUntilHalt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Game getGame() {
        return game;
    }
}
