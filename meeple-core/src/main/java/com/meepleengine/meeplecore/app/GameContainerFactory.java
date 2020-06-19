package com.meepleengine.meeplecore.app;

import java.util.List;

public interface GameContainerFactory {

    GameContainer create(String drlFilePath, String assetsFilePath, List<String> playerIds) throws Exception;

}
