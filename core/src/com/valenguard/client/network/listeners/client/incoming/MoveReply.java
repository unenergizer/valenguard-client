package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.valenguard.client.Valenguard;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.constants.Direction;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
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

//    private Interpolation interpolation = Interpolation.linear;
//
//    @Opcode(getOpcode = Opcodes.MOVE_REPLY)
//    public void onMoveReply(ServerHandler serverHandler) throws IOException {
//
//        ObjectInputStream inputStream = serverHandler.getInputStream();
//
//        final boolean canMove = inputStream.readBoolean();
//
//        final PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();
//        final int tileToX = inputStream.readInt();
//        final int tileToY = inputStream.readInt();
//
//        Gdx.app.debug(TAG, "Can Move? " + canMove + " X: " + tileToX + ", Y: " + tileToY);
//
//        playerClient.setFutureX(tileToX);
//        playerClient.setFutureY(tileToY);
//    }
//
//    private float getInterpolation(float delta, float elapsed, int lifeTime) {
//        elapsed += delta;
//
//        float progress = Math.min(1f, elapsed/lifeTime);
//        return interpolation.apply(progress);
//    }

    private float deltaTimeElapsed = 0.0f;

    private long startTime = 0;

    private Timer currentTimer;

    @Opcode(getOpcode = Opcodes.MOVE_REPLY)
    public void onMoveReply(ServerHandler serverHandler) throws IOException {

        ObjectInputStream inputStream = serverHandler.getInputStream();

        final boolean canMove = inputStream.readBoolean();

        Gdx.app.debug(TAG, "Can Move? " + canMove);

        if (!canMove) return;

        PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();

        playerClient.setMoving(true);

        final int tileToX = inputStream.readInt();
        final int tileToY = inputStream.readInt();
        int tileDifx = tileToX - playerClient.getTileX();
        int tileDify = tileToY - playerClient.getTileY();

        //if (tileDifx == +2) tileDifx = arrivedLateClamp(tileDifx, tileToX, playerClient, Direction.RIGHT);
        //if (tileDifx == -2) tileDifx = arrivedLateClamp(tileDifx, tileToX, playerClient, Direction.LEFT);
        //if (tileDify == +2) tileDify = arrivedLateClamp(tileDify, tileToY, playerClient, Direction.UP);
        //if (tileDify == -2) tileDify = arrivedLateClamp(tileDify, tileToY, playerClient, Direction.DOWN);

        /**
         * The following commented out code was an attempt at
         * interpolation. It only sorta worked but was buggy.
         * Feel free to mess around with it.
         */

        final float TILES_PER_SECOND = 1.0f;
        final int useableTileDifx = tileDifx;
        final int useableTileDify = tileDify;

        startTime = System.currentTimeMillis();

        // Update the client with server coordinates.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                // Run a timer for 1 second and interpolate the player
                // movement.
                currentTimer = new Timer().runForPeriod(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer tick) {

                        float movedTilePerTick = deltaTimeElapsed * ClientConstants.TILE_SIZE * TILES_PER_SECOND;

                        PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();
                        playerClient.setDrawX(playerClient.getTileX() * ClientConstants.TILE_SIZE + movedTilePerTick * useableTileDifx);
                        playerClient.setDrawY(playerClient.getTileY() * ClientConstants.TILE_SIZE + movedTilePerTick * useableTileDify);

                        // Clamp the player onto the new tile.
                        if (tick == 59) {
                            System.out.println();
                            playerClient.setDrawX(tileToX * ClientConstants.TILE_SIZE);
                            playerClient.setDrawY(tileToY * ClientConstants.TILE_SIZE);
                            playerClient.setTileX(tileToX);
                            playerClient.setTileY(tileToY);
                            deltaTimeElapsed = 0.0f;
                            long endTime = System.currentTimeMillis();
                            System.out.println("# OF MILLISECONDS: " + (endTime - startTime));
                            playerClient.setMoving(false);
                        }
                        deltaTimeElapsed += Gdx.graphics.getRawDeltaTime();
                    }
                }, Timer.SECOND).start();
            }
        });
    }

    private int arrivedLateClamp(int tileDif, int tileTo, PlayerClient playerClient, Direction direction) {
        currentTimer.cancel();
        boolean greaterThanZero = tileDif > 0;
        tileDif = greaterThanZero ? tileDif - 1 : tileDif + 1;
        deltaTimeElapsed = 0.0f;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            playerClient.setDrawY((tileTo + (greaterThanZero ? -1 : +1)) * ClientConstants.TILE_SIZE);
            playerClient.setTileY(tileTo + (greaterThanZero ? -1 : +1));
        } else {
            playerClient.setDrawX((tileTo + (greaterThanZero ? -1 : +1)) * ClientConstants.TILE_SIZE);
            playerClient.setTileX(tileTo + (greaterThanZero ? -1 : +1));
        }
        return tileDif;
    }
}
