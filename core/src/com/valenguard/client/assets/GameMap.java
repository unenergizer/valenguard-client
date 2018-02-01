package com.valenguard.client.assets;

import com.badlogic.gdx.Game;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 12/20/2017 @ 12:10 AM
 * ______________________________________________________
 *
 * Copyright © 2017 Valenguard.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code
 * and/or source may be reproduced, distributed, or
 * transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or
 * mechanical methods, without the prior written
 * permission of the owner.
 *******************************************************/

public enum GameMap {

    MAIN_TOWN("maintown.tmx"),
    SOUTH("south.tmx"),
    NORTH("north.tmx");

    private String filePath;

    GameMap(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return "maps/" + filePath;
    }

    /**
     * Returns the map associated with the map name.
     *
     * @param mapName The map name. Make sure to include .tmx in the map name
     * @return The map
     */
    public static GameMap getMapByName(String mapName) {
        for (GameMap gameMap : GameMap.values()) {
            if (mapName.equals(gameMap.filePath)) return gameMap;
        }
        throw new RuntimeException("Failed to get filepath for map by name: " + mapName);
    }
}
