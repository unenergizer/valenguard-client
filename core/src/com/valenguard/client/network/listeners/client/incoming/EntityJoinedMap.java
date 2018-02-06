package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;

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

public class EntityJoinedMap implements Listener {

    private static final String TAG = EntityJoinedMap.class.getSimpleName();

    @Opcode(getOpcode = Opcodes.ENTITY_JOINED_MAP)
    public void onEntityJoinedMap(ServerHandler serverHandler) {
        final int entityId = serverHandler.readInt();
        final int x = serverHandler.readInt();
        final int y = serverHandler.readInt();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.debug(TAG, "ENTITY JOINED: " + entityId + ". X: " + x + ", Y: " + y);

                // Don't add the ClientPlayer to the entities list.
                if (Valenguard.getInstance().getPlayerClient().getEntityId() == entityId) {
                    Gdx.app.debug(TAG, "DID NOT ADD: " + entityId + ".");
                    return;
                }

                Gdx.app.debug(TAG, "ADDED ENTITY: " + entityId + ". X: " + x + ", Y: " + y);
                Valenguard.getInstance().getGameScreen().getEntityList().add(new Entity(entityId, x, y, 1));
            }
        });
    }
}
