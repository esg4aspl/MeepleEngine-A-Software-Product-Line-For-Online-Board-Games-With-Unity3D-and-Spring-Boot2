package com.meepleengine.meepleserver.controller;

import com.meepleengine.meepleserver.model.dto.LobbyDTO;
import com.meepleengine.meepleserver.model.request.CreateLobbyRequest;
import com.meepleengine.meepleserver.service.LobbyService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping
    public List<LobbyDTO> getAll() {
        return lobbyService.getAll();
    }

    @PostMapping
    public LobbyDTO create(@RequestBody CreateLobbyRequest lobbyForm) {
        String userIdHex = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = new ObjectId(userIdHex);
        return lobbyService.create(userId, lobbyForm);
    }

    @PostMapping("/join")
    public void join(@RequestParam(name = "id") String lobbyIdHex) {
        String userIdHex = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = new ObjectId(userIdHex);
        ObjectId lobbyId = new ObjectId(lobbyIdHex);
        lobbyService.join(userId, lobbyId);
    }

    @PostMapping("/exit")
    public void exit() {
        String userIdHex = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = new ObjectId(userIdHex);
        lobbyService.exit(userId);
    }

    @PostMapping("/start")
    public void start(@RequestParam(name = "id") String lobbyIdHex) {
        ObjectId lobbyId = new ObjectId(lobbyIdHex);
        lobbyService.start(lobbyId);
    }

}
