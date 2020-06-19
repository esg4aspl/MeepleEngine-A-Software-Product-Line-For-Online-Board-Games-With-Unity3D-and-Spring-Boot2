package com.meepleengine.meepleserver.converter.doc2model;

import com.meepleengine.meepleserver.model.User;
import com.meepleengine.meepleserver.util.DateUtil;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Doc2UserConverter implements Function<Document, User> {

    @Override
    public User apply(Document document) {
        User user = new User();
        user.setId(document.getObjectId("_id"));
        user.setUsername(document.getString("username"));
        user.setPassword(document.getString("password"));
        user.setLobbyId(document.getObjectId("lobbyId"));
        user.setLmd(DateUtil.asLocalDateTime(document.getDate("lmd")));
        return user;
    }

}
