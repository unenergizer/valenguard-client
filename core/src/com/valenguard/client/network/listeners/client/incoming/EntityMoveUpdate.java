package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.Valenguard;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.util.Consumer;
import com.valenguard.client.util.Timer;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/20/2018 @ 1:57 PM
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

public class EntityMoveUpdate implements Listener {


    private static final String TAG = EntityMoveUpdate.class.getSimpleName();

    @Opcode(getOpcode = Opcodes.ENTITY_MOVE_UPDATE)
    public void onEntityMoveUpdate(ServerHandler serverHandler) {
        System.out.println("ENTITY MOVE UPDATE CALLED");
        final int entityID = serverHandler.readInt();
        final int x = serverHandler.readInt();
        final int y = serverHandler.readInt();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.debug(TAG, "EntityMoveUpdate: " + entityID + ". X: " + x + ", Y: " + y);

                for (Entity entity : Valenguard.getInstance().getGameScreen().getEntityList()) {
                    if (entity.getEntityId() == entityID) {
                        Gdx.app.debug(TAG, "Moving entity: " + entityID + " X: " + x + ", Y: " + y);
                        entity.setX(x);
                        entity.setY(y);
                    }
                }
            }
        });
    }
}
