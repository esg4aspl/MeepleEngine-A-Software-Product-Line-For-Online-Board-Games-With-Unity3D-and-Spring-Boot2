package com.meepleengine.meeplecore.api;

import com.meepleengine.meeplecore.model.GameMessage;

public interface IGameObserver {

    /**
     *
     * @param gameMessage
     */
    void onAction(GameMessage gameMessage);

}