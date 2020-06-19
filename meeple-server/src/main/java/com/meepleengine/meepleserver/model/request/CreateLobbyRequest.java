package com.meepleengine.meepleserver.model.request;

import javax.validation.constraints.NotBlank;

public class CreateLobbyRequest {

    @NotBlank
    private String lobbyName;
    @NotBlank
    private String gameName;

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
}
