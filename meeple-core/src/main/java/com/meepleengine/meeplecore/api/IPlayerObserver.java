package com.meepleengine.meeplecore.api;
import com.meepleengine.meeplecore.model.PlayerMessage;

public interface IPlayerObserver {

    /**
     *
     * @param message
     */
    void onAction(PlayerMessage message);

}
