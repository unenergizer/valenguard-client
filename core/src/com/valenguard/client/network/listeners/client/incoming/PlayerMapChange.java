package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.GameMap;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.screens.GameScreen;

import java.io.IOException;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/27/2018 @ 8:51 PM
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

public class PlayerMapChange implements Listener {

    private static final String TAG = PlayerMapChange.class.getSimpleName();

    @Opcode(getOpcode = Opcodes.PLAYER_MAP_CHANGE)
    public void onPlayerMapChange(ServerHandler serverHandler) {

        final String mapName = serverHandler.readString();
        final int x = serverHandler.readInt();
        final int y = serverHandler.readInt();

        System.out.println("The player should switch to map: " + mapName);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Valenguard valenguard = Valenguard.getInstance();
                GameScreen gameScreen = valenguard.getGameScreen();

                // Clearing all current entities since they are switching maps
                gameScreen.getEntityList().clear();

                // Switching the map that is to be rendered
                gameScreen.setTiledMap(GameMap.getMapByName(mapName));

                // Setting up the player to have a new location on the new map
                PlayerClient playerClient = valenguard.getPlayerClient();
                playerClient.setX(x);
                playerClient.setY(y);
            }
        });
    }
}
