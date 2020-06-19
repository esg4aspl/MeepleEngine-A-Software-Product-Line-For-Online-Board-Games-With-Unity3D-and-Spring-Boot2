package com.meepleengine.meepleserver.converter.doc2model;

import com.meepleengine.meepleserver.model.Lobby;
import com.meepleengine.meepleserver.util.DateUtil;
import com.mongodb.Function;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class Doc2LobbyConverter implements Function<Document, Lobby> {

    @Override
    public Lobby apply(Document document) {
        Lobby lobby = new Lobby();
        lobby.setId(document.getObjectId("_id"));
        lobby.setLobbyName(document.getString("lobbyName"));
        lobby.setGameName(document.getString("gameName"));
        lobby.setStatus(document.getString("status"));
        lobby.setUsers(document.getList("users", ObjectId.class));
        lobby.setLmd(DateUtil.asLocalDateTime(document.getDate("lmd")));
        return lobby;
    }

}
