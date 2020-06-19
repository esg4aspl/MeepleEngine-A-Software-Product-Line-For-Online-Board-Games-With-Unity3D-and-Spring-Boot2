package com.meepleengine.meepleserver.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class User {

    private ObjectId id;
    private String username;
    private String password;
    private ObjectId lobbyId;
    private LocalDateTime lmd;

    public User() {}

    public User(String username, String password) {
        this.id = new ObjectId();
        this.username = username;
        this.password = password;
        this.lobbyId = null;
        this.lmd = LocalDateTime.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ObjectId getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(ObjectId lobbyId) {
        this.lobbyId = lobbyId;
    }

    public LocalDateTime getLmd() {
        return lmd;
    }

    public void setLmd(LocalDateTime lmd) {
        this.lmd = lmd;
    }
}
