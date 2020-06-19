package com.meepleengine.meeplecore.core.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.meepleengine.meeplecore.api.parser.MeepleParser;
//import com.meepleengine.meeplecore.core.engine.SimpleGame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultMeepleParserTest {

    private final String gameDocument = "{}";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void it_should_parse_given_content_to_game() throws JsonProcessingException {
//        // Given
//        MeepleParser parser = new DefaultMeepleParser();
//
//        // When
//        SimpleGame simpleGame = parser.parse(gameDocument);
//
//        // Then
//        assertEquals("name", simpleGame.toString());
    }

    @Test
    public void it_should_convert_given_json_to_game() throws JsonProcessingException {
//        // Given
//        MeepleParser parser = new DefaultMeepleParser();
//        JsonNode gameNode = objectMapper.readTree(gameDocument);
//
//        // When
//        SimpleGame simpleGame = parser.readGame(gameNode);
//
//        // Then
//        assertEquals("name", simpleGame.toString());
    }
}