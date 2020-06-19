package com.meepleengine.meepleserver.repository;

import com.meepleengine.meepleserver.converter.doc2model.Doc2UserConverter;
import com.meepleengine.meepleserver.converter.model2doc.User2DocConverter;
import com.meepleengine.meepleserver.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class UserRepository {

    private final MongoCollection<Document> userCollection;
    private final Doc2UserConverter doc2UserConverter;
    private final User2DocConverter user2DocConverter;

    @Autowired
    public UserRepository(MongoCollection<Document> userCollection,
                          Doc2UserConverter doc2UserConverter,
                          User2DocConverter user2DocConverter) {
        this.userCollection = userCollection;
        this.doc2UserConverter = doc2UserConverter;
        this.user2DocConverter = user2DocConverter;
    }

    public User get(ObjectId userId) {
        Document userDocument = userCollection.find(eq("_id", userId)).first();

        if(userDocument == null) {
            return null;
        }

        return doc2UserConverter.apply(userDocument);
    }


    public User get(String username) {
        Document userDocument = userCollection.find(eq("username", username)).first();

        if(userDocument == null) {
            return null;
        }

        return doc2UserConverter.apply(userDocument);
    }

    public void insert(User user) {
        userCollection.insertOne(user2DocConverter.apply(user));
    }

    public void upsert(User user) {
        userCollection.replaceOne(
                eq("_id", user.getId()),
                user2DocConverter.apply(user),
                new ReplaceOptions().upsert(true));
    }
}
