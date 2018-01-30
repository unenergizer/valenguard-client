package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;

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


            String entityId = serverHandler.readString();
            int x = serverHandler.readInt();
            int y = serverHandler.readInt();


        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
