package com.meepleengine.meepleserver.converter.model2doc;

import com.meepleengine.meepleserver.model.User;
import com.meepleengine.meepleserver.util.DateUtil;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class User2DocConverter implements Function<User, Document> {

    @Override
    public Document apply(User user) {
        return new Document("_id", user.getId())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("lobbyId", user.getLobbyId())
                .append("lmd", DateUtil.now());
    }

}
