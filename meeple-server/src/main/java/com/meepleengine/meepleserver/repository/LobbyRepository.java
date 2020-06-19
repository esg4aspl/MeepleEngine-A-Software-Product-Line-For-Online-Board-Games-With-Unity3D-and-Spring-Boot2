package com.meepleengine.meepleserver.repository;

import com.meepleengine.meepleserver.converter.doc2model.Doc2LobbyConverter;
import com.meepleengine.meepleserver.converter.model2doc.Lobby2DocConverter;
import com.meepleengine.meepleserver.model.Lobby;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class LobbyRepository {

    private final MongoCollection<Document> lobbyCollection;
    private final Doc2LobbyConverter doc2LobbyConverter;
    private final Lobby2DocConverter lobby2DocConverter;

    @Autowired
    public LobbyRepository(MongoCollection<Document> lobbyCollection,
                           Doc2LobbyConverter doc2LobbyConverter,
                           Lobby2DocConverter lobby2DocConverter) {
        this.lobbyCollection = lobbyCollection;
        this.doc2LobbyConverter = doc2LobbyConverter;
        this.lobby2DocConverter = lobby2DocConverter;
    }

    public Lobby getById(ObjectId lobbyId) {
        return getFiltered(eq("_id", lobbyId));
    }

    private Lobby getFiltered(Bson filter) {
        Document lobbyDocument = lobbyCollection.find(filter).first();

        if(lobbyDocument == null) {
            return null;
        }

        return doc2LobbyConverter.apply(lobbyDocument);
    }

    public List<Lobby> getAll() {
        return getAllFiltered(new BsonDocument());
    }

    private List<Lobby> getAllFiltered(Bson filter) {
        List<Lobby> lobbyList = new ArrayList<>();
        MongoCursor<Document> cursor = lobbyCollection.find(filter).iterator();
        try {
            while (cursor.hasNext()) {
                lobbyList.add(doc2LobbyConverter.apply(cursor.next()));
            }
        } finally {
            cursor.close();
        }

        return lobbyList;
    }

    public void insert(Lobby lobby) {
        lobbyCollection.insertOne(lobby2DocConverter.apply(lobby));
    }

    public void upsert(Lobby lobby) {
        lobbyCollection.replaceOne(
                eq("_id", lobby.getId()),
                lobby2DocConverter.apply(lobby),
                new ReplaceOptions().upsert(true));
    }

    public void delete(Lobby lobby) {
        lobbyCollection.deleteOne(lobby2DocConverter.apply(lobby));
    }
}
