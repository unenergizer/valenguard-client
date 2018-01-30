package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
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

public class EntityExitMap implements Listener {

    private static final String TAG = EntityExitMap.class.getSimpleName();

    @Opcode(getOpcode = Opcodes.ENTITY_EXIT_MAP)
    public void onEntityExitMap(ServerHandler serverHandler) {
        final int entityId = serverHandler.readInt();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                for (Entity entity : Valenguard.getInstance().getGameScreen().getEntityList()) {
                    if (entity.getEntityId() == entityId) {
                        Gdx.app.debug(TAG, "REMOVING ENTITY: " + entityId);
                        Valenguard.getInstance().getGameScreen().getEntityList().remove(entity);
                    }
                }
            }
        });
    }
}
