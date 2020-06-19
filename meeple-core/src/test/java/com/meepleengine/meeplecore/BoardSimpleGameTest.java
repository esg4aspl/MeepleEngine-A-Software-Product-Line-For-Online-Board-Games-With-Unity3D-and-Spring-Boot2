package com.meepleengine.meeplecore;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardSimpleGameTest {

    @Test
    public void it_should_parse_and_run_dixit() throws JsonProcessingException {
//
//        MeepleParser meepleParser = new DefaultMeepleParser();
//
//        Game dixitGame = meepleParser.parse("{}");
//
//        PlayerListener player1Listener = new PlayerListener() {
//            @Override
//            public void onAssets(List<Item> assets) {
//
//            }
//
//            @Override
//            public void onState() {
//
//            }
//
//            @Override
//            public void onAvailableItems(List<Item> availableItems) {
//
//            }
//
//            @Override
//            public void onCommand(Command command) {
//
//            }
//        };
//
//        PlayerListener player2Listener = new PlayerListener() {
//            @Override
//            public void onAssets(List<Item> assets) {
//
//            }
//
//            @Override
//            public void onState() {
//
//            }
//
//            @Override
//            public void onAvailableItems(List<Item> availableItems) {
//
//            }
//
//            @Override
//            public void onCommand(Command command) {
//
//            }
//        };
//
//        dixitGame.attachPlayer(0, player1Listener);
//        dixitGame.attachPlayer(1, player2Listener);
//
//        dixitGame.start();

//        List<Item> turn1AvailableItems = dixitGame.getAvailableItems(player1);
//
//        // -> output
//        System.out.println("turn1AvailableItems: " + turn1AvailableItems.toString());
//
//        // <- input
//        Item selectedItem = turn1AvailableItems.get(0);
//
//        List<Command> turn1AvailableCommands = dixitGame.getAvailableCommands(player1, selectedItem);
//
//        // -> output
//        System.out.println("turn1AvailableCommands: " + turn1AvailableCommands.toString());
//
//        // <- input
//        Command selectedCommand = turn1AvailableCommands.get(0);
//
//        boolean turn1CommandStatus = dixitGame.applyCommand(player1, selectedItem, selectedCommand);
//
//        // -> output
//        System.out.println("turn1CommandStatus: " + turn1CommandStatus);
//
//        // turn ended internally



    }

    // This is not an unit test! Written for just development purposes.
    @Test
    public void it_should_test_board_game() {

//        Grid grid1 = new SimpleGrid();
//        Grid grid2 = new SimpleGrid();
//
//        Board board = new SimpleBoard(Arrays.asList(grid1, grid2));
//
//        SimpleItem simpleItem1 = new SimpleItem();
//        SimpleItem simpleItem2 = new SimpleItem();
//
//        SimplePlayer simplePlayer1 = new SimplePlayer();
//        SimplePlayer simplePlayer2 = new SimplePlayer();
//
//        action action1 = new action();
//        action action2 = new action();
//        action action3 = new action();
//
//        Stage stage_start_1 = new Stage(Collections.<action>emptyList());
//        Stage stage_loop_1 = new Stage(Arrays.asList(action1, action2));
//        Stage stage_loop_2 = new Stage(Arrays.asList(action1, action3));
//        Stage stage_end_1 = new Stage(Arrays.asList(action3));
//
//        Phase startPhase = new Phase(Arrays.asList(stage_start_1));
//        Phase loopPhase = new Phase(Arrays.asList(stage_loop_1,stage_loop_2));
//        Phase endPhase = new Phase(Arrays.asList(stage_end_1));

//        SimpleGame game = new SimpleGame(
//                board,
//                Arrays.asList(simpleItem1, simpleItem2),
//                Arrays.asList(simplePlayer1, simplePlayer2),
//                Arrays.asList(startPhase, loopPhase, endPhase)
//        );

//        game.runStep(player, action);

    }

}
