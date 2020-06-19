package com.meepleengine.meepleserver.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public class Lobby {

    private ObjectId id;
    private String lobbyName;
    private String gameName;
    private String status;
    private List<ObjectId> users;
    private LocalDateTime lmd;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ObjectId> getUsers() {
        return users;
    }

    public void setUsers(List<ObjectId> users) {
        this.users = users;
    }

    public LocalDateTime getLmd() {
        return lmd;
    }

    public void setLmd(LocalDateTime lmd) {
        this.lmd = lmd;
    }
}
