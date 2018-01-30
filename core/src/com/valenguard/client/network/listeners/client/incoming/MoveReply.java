package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;

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

    @Opcode(getOpcode = Opcodes.MOVE_REPLY)
    public void onMoveReply(ServerHandler serverHandler) throws IOException {

        ObjectInputStream inputStream = serverHandler.getInputStream();

        final boolean canMove = inputStream.readBoolean();
        final int x = inputStream.readInt();
        final int y = inputStream.readInt();

        Gdx.app.debug(TAG, "Can Move? " + canMove);

        // Update the client with server coordinates.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Valenguard.getInstance().getPlayerClient().setX(x);
                Valenguard.getInstance().getPlayerClient().setY(y);
            }
        });
    }
}
