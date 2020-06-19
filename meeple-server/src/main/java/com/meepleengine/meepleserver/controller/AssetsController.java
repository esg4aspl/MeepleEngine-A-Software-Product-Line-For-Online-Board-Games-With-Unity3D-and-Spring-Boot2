package com.meepleengine.meepleserver.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.meepleengine.meepleserver.service.AssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetsController {

    private final AssetsService assetsService;

    @Autowired
    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @GetMapping()
    public JsonNode getAssets(@RequestParam("game") String gameName) {
        return assetsService.getAssets(gameName);
    }

}
