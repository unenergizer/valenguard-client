package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.settings.UserSettings;
import com.valenguard.client.util.Consumer;
import com.valenguard.client.util.Timer;

import java.io.IOException;
import java.io.ObjectInputStream;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/8/2018 @ 5:28 PM
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

public class MoveReply implements Listener {

    private static final String TAG = MoveReply.class.getSimpleName();

    private float deltaTimeElapsed = 0.0f;

    @Opcode(getOpcode = Opcodes.MOVE_REPLY)
    public void onMoveReply(ServerHandler serverHandler) throws IOException {

        ObjectInputStream inputStream = serverHandler.getInputStream();

        final boolean canMove = inputStream.readBoolean();

        Gdx.app.debug(TAG, "Can Move? " + canMove);

        PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();
        final int tileToX = inputStream.readInt();
        final int tileToY = inputStream.readInt();
        final int tileDifx = tileToX - (int)(playerClient.getX());
        final int tileDify = tileToY - (int)(playerClient.getY());

        /**
         * The following commented out code was an attempt at
         * interpolation. It only sorta worked but was buggy.
         * Feel free to mess around with it.
         */

        final float TILES_PER_SECOND = 1.0f;

        // Update the client with server coordinates.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();
                playerClient.setX(tileToX);
                playerClient.setY(tileToY);

                // Run a timer for 1 second and interpolate the player
                // movement.
                new Timer().runForPeriod(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer tick) {

                        float speed = deltaTimeElapsed * (TILES_PER_SECOND * (float)ClientConstants.TILE_SIZE);

                        System.out.println("speed: " + speed);

                        PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();
                        playerClient.setX((float)Math.floor(playerClient.getX()) + speed*tileDifx/16.0f);
                        playerClient.setY((float)Math.floor(playerClient.getY()) + speed*tileDify/16.0f);

                        // Clamp the player onto the new tile.
                        if (tick == 59) {
                            playerClient.setX(tileToX);
                            playerClient.setY(tileToY);
                            deltaTimeElapsed = 0.0f;
                        }
                        deltaTimeElapsed += Gdx.graphics.getDeltaTime();
                    }
                }, Timer.SECOND).start();
            }
        });
    }
}
