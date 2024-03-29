package com.valenguard.client.entities;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.maps.MapData;
import com.valenguard.client.util.Consumer;
import com.valenguard.client.util.Timer;

import lombok.Getter;
import lombok.Setter;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/28/2018 @ 4:49 PM
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

@Getter
@Setter
public class PlayerClient extends Entity {

    private static final String TAG = PlayerClient.class.getSimpleName();

    /**
     * The current TiledMap the player is on.
     */
    private MapData currentMap;

    /**
     * Defines whither or not the player is traversing across the map.
     */
    private boolean moving = false;

    private Timer preMovementTimer;

    private float preMovement;

    public PlayerClient(int entityId, int tileX, int tileY, double speed) {
        super(entityId, tileX, tileY, speed);
        Gdx.app.debug(TAG, "EntityID: " + entityId + ", X: " + tileX + ", Y: " + tileY);
    }

    public void startPreMovement(final int tileToX, final int tileToY) {
        if (preMovementTimer != null) preMovementTimer.cancel();
        preMovementTimer = new Timer().runForPeriod(new Consumer<Integer>() {
            @Override
            public void accept(Integer data) {
                preMovement = getMovementDeltaTimeElapsed(); // Getting the amount they have currently moved by up to this point
                movementPerTick(tileToX - getTileX(), tileToY - getTileY(), 0.0f);
                preMovement = getMovementDeltaTimeElapsed();
            }
        }, Timer.MINUTE).start();
    }
}
