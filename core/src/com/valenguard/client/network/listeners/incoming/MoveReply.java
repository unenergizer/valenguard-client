package com.valenguard.client.network.listeners.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.network.ServerHandle;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.screens.GameScreen;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
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

    @Opcode(getOpcode = Opcodes.MOVE_REPLY)
    public void onMoveReply(ServerHandle serverHandle) {

        final byte moveDirection = serverHandle.readByte();
        System.out.println(Byte.toString(serverHandle.readByte()));

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                float moveX = 0;
                float moveY = 0;

                switch (moveDirection) {
                    case (byte) 0x01:
                        moveY = 10;
                        break;
                    case (byte) 0x02:
                        moveY = -10;
                        break;
                    case (byte) 0x03:
                        moveX = -10;
                        break;
                    case (byte) 0x04:
                        moveX = 10;
                        break;
                }

                GameScreen screen = (GameScreen) Valenguard.getInstance().getScreen();
                float x = screen.getPlayer().getX();
                float y = screen.getPlayer().getY();
                screen.getPlayer().setX(x + moveX);
                screen.getPlayer().setY(y + moveY);

                System.out.println("[MOVE] x: " + x + " y: " + y);
            }
        });
    }
}
