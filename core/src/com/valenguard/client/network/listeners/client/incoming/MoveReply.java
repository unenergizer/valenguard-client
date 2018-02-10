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

        playerClient.move(tileToX, tileToY, 0.0f);
    }
}
