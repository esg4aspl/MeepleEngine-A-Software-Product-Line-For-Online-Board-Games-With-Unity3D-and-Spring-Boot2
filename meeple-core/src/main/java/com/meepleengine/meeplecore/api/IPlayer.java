package com.meepleengine.meeplecore.api;
import com.fasterxml.jackson.databind.JsonNode;

public interface IPlayer {

    /**
     *
     * @param actionDetails
     */
    void action(String name, JsonNode actionDetails);

    /**
     *
     * @param IPlayerObserver
     */
    void registerObserver(IPlayerObserver IPlayerObserver);

}
