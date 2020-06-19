package com.meepleengine.meepleserver.service;

import com.meepleengine.meepleserver.exception.BusinessException;
import com.meepleengine.meepleserver.model.Lobby;
import com.meepleengine.meepleserver.model.User;
import com.meepleengine.meepleserver.model.dto.LobbyDTO;
import com.meepleengine.meepleserver.model.request.CreateLobbyRequest;
import com.meepleengine.meepleserver.repository.LobbyRepository;
import com.meepleengine.meepleserver.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final GameService gameService;
    private final UserRepository userRepository;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository,
                        GameService gameService,
                        UserRepository userRepository) {
        this.lobbyRepository = lobbyRepository;
        this.gameService = gameService;
        this.userRepository = userRepository;
    }

    public List<LobbyDTO> getAll() {
        return lobbyRepository.getAll().stream().map(lobby -> {
            LobbyDTO lobbyDTO = new LobbyDTO();
            lobbyDTO.setId(lobby.getId().toHexString());
            lobbyDTO.setLobbyName(lobby.getLobbyName());
            lobbyDTO.setGameName(lobby.getGameName());
            return lobbyDTO;
        }).collect(Collectors.toList());
    }

    public LobbyDTO create(ObjectId userId, CreateLobbyRequest createLobbyRequest) {
        User user = userRepository.get(userId);

        if (user.getLobbyId() != null) {
            throw new BusinessException("Already in a room!");
        }

        Lobby lobby = new Lobby();
        lobby.setId(ObjectId.get());
        lobby.setLobbyName(createLobbyRequest.getLobbyName());
        lobby.setGameName(createLobbyRequest.getGameName());
        lobby.setStatus("IN_LOBBY");
        lobby.setUsers(Arrays.asList(userId));
        lobbyRepository.insert(lobby);

        user.setLobbyId(lobby.getId());
        userRepository.upsert(user);

        LobbyDTO lobbyDTO = new LobbyDTO();
        lobbyDTO.setId(lobby.getId().toHexString());
        lobbyDTO.setLobbyName(lobby.getLobbyName());
        lobbyDTO.setGameName(lobby.getGameName());
        return lobbyDTO;
    }

    public void join(ObjectId userId, ObjectId lobbyId) {
        User user = userRepository.get(userId);

        if (user.getLobbyId() != null) {
            throw new BusinessException("Already in a room!");
        }

        Lobby lobby = lobbyRepository.getById(lobbyId);

        if(lobby == null) {
            throw new BusinessException("Lobby does not exist!");
        }

        if(lobby.getUsers().contains(userId)) {
            throw new BusinessException("Already joined!");
        }

        lobby.getUsers().add(userId);
        lobbyRepository.upsert(lobby);

        user.setLobbyId(lobby.getId());
        userRepository.upsert(user);
    }

    public void exit(ObjectId userId) {
        User user = userRepository.get(userId);

        if (user.getLobbyId() == null) {
            throw new BusinessException("Not in a room!");
        }

        Lobby lobby = lobbyRepository.getById(user.getLobbyId());

        if(lobby == null) {
            throw new BusinessException("Lobby does not exist!");
        }

        if(!lobby.getUsers().contains(userId)) {
            throw new BusinessException("Already not joined!");
        }

        lobby.getUsers().remove(userId);
        lobbyRepository.upsert(lobby);

        user.setLobbyId(null);
        userRepository.upsert(user);
    }

    public void start(ObjectId lobbyId) {
        Lobby lobby = lobbyRepository.getById(lobbyId);

        if(lobby == null) {
            throw new BusinessException("Lobby does not exist!");
        }

        lobby.setStatus("PLAYING");
        lobbyRepository.upsert(lobby);

        gameService.createGame(lobby);
    }

}

