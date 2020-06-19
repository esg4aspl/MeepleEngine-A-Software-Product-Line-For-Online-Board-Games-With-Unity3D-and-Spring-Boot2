package com.meepleengine.meepleserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleengine.meepleserver.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class AssetsService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getAssets(String gameName) {
        File file = new File("src/main/resources/assets/" + gameName + ".json");
        try {
            return objectMapper.readTree(file);
        } catch (IOException e) {
            throw new BusinessException("Could not read assets! gameName: " + gameName);
        }
    }

}
