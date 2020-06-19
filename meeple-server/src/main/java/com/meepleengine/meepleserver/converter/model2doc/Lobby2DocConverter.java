package com.meepleengine.meepleserver.converter.model2doc;

import com.meepleengine.meepleserver.model.Lobby;
import com.meepleengine.meepleserver.util.DateUtil;
import org.springframework.stereotype.Component;
import org.bson.Document;
import java.util.function.Function;

@Component
public class Lobby2DocConverter implements Function<Lobby, Document> {

    @Override
    public Document apply(Lobby lobby) {
        return new Document("_id", lobby.getId())
                .append("lobbyName", lobby.getLobbyName())
                .append("gameName", lobby.getGameName())
                .append("status", lobby.getStatus())
                .append("users", lobby.getUsers())
                .append("lmd", DateUtil.now());
    }

}
