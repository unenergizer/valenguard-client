package com.valenguard.client.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.valenguard.client.Valenguard;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.util.Consumer;
import com.valenguard.client.util.Timer;

import lombok.Getter;
import lombok.Setter;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 12/20/2017 @ 12:10 AM
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

@Setter
@Getter
public class Entity {

    private static final String TAG = Entity.class.getSimpleName();

    /**
     * A unique ID given to this entity by the server.
     */
    private int entityId;

    /**
     * The exact tile location the entity should be at after or before moving.
     */
    private int tileX, tileY, futureX, futureY;

    /**
     * The rate at which an entity can move across tiles.
     */
    private double speed;

    /**
     * The actual position on the screen. During walking animations these values
     * will not equal tileX and tileY. However, while the entity is standing still,
     * drawX should equal tileX and drawY should equal tileY.
     */
    private float drawX, drawY;

    private Timer movementTimer;
    private float movementDeltaTimeElapsed = 0.0f;

    private float TILES_PER_SECOND = 1.0f;

    public Entity(int entityId, int tileX, int tileY, double speed) {
        this.entityId = entityId;
        this.tileX = tileX;
        this.tileY = tileY;
        this.speed = speed;

        // set default futures
        futureX = tileX;
        futureY = tileY;

        // set default draw locations
        drawX = tileX * ClientConstants.TILE_SIZE;
        drawY = tileY * ClientConstants.TILE_SIZE;
    }

    public void move(final int tileToX, final int tileToY, final float distanceMoved) {

        movementDeltaTimeElapsed = 0.0f;

        int tileDifx = tileToX - tileX;
        int tileDify = tileToY - tileY;

        /**
         * The following commented out code was an attempt at
         * interpolation. It only sorta worked but was buggy.
         * Feel free to mess around with it.
         */

        final int useableTileDifx = tileDifx;
        final int useableTileDify = tileDify;

        final Entity thisEntity = this;

        // Update the client with server coordinates.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                // Run a timer for 1 second and interpolate the player
                // movement.
                movementTimer = new Timer().runForPeriod(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer tick) {

                        System.out.println("SUBTRACTING OUT: " + (1/60f * distanceMoved));
                        movementPerTick(useableTileDifx, useableTileDify, 1/60f * distanceMoved);

                        // Clamp the player onto the new tile.
                        if (tick == 59) {
                            System.out.println();
                            setDrawX(tileToX * ClientConstants.TILE_SIZE);
                            setDrawY(tileToY * ClientConstants.TILE_SIZE);
                            setTileX(tileToX);
                            setTileY(tileToY);
                            movementDeltaTimeElapsed = 0.0f;
                            if (thisEntity instanceof PlayerClient) {
                                System.out.println("Settings movement to false.");
                                ((PlayerClient) thisEntity).setMoving(false);
                            }
                        }
                    }
                }, Timer.MINUTE).start();
            }
        });
    }

    protected void movementPerTick(final int useableTileDifx, final int useableTileDify, float movementSubtraction) {
        float movedTilePerTick = movementDeltaTimeElapsed * ClientConstants.TILE_SIZE * TILES_PER_SECOND - movementSubtraction;
        setDrawX(getTileX() * ClientConstants.TILE_SIZE + movedTilePerTick * useableTileDifx);
        setDrawY(getTileY() * ClientConstants.TILE_SIZE + movedTilePerTick * useableTileDify);
        movementDeltaTimeElapsed += Gdx.graphics.getRawDeltaTime();
    }

    /**
     * Draws a texture to the clients screen.
     *
     * @param batch   The texture renderer.
     * @param texture The texture to draw.
     */
//    public void draw(Batch batch, Texture texture) {
//
//        // No movement. No calculations needed.
//        if (tileX == futureX && tileY == futureY) {
//            drawX = tileX * ClientConstants.TILE_SIZE;
//            drawY = tileY * ClientConstants.TILE_SIZE;
//            batch.draw(texture, drawX, drawY);
//            return;
//        }
//
//        double destinationX = futureX - tileX;
//        double destinationY = futureY - tileY;
//
//        double distance = Math.sqrt(destinationX * destinationX + destinationY * destinationY);
//        destinationX = destinationX / distance;
//        destinationY = destinationY / distance;
//
//        double travelX = destinationX * this.speed;
//        double travelY = destinationY * this.speed;
//
//        distanceTraveled += Math.sqrt(travelX * travelX + travelY * travelY);
//
//        if (distanceTraveled >= distance) {
//            System.out.println("Destination Reached!!!");
//            drawX = (float) destinationX;
//            drawY = (float) destinationY;
//
//            // update tile x & y values
//            tileX = futureX;
//            tileY = futureY;
//
//            distanceTraveled = 0;
//        } else {
//            System.out.println("DrawX: " + drawX + ", DrawY: " + drawY + ", DestinationX: " + destinationX + ", DestinationY: " + destinationY + ", Distance: " + distance + ", DistanceTraveled: " + distanceTraveled);
//
//            drawX += travelX;
//            drawY += travelY;
//        }
//
//        // draw
//        batch.draw(texture, drawX * ClientConstants.TILE_SIZE, drawY * ClientConstants.TILE_SIZE);
//    }
    public void draw(Batch batch, Texture texture) {
        //System.out.println("DrawX: " + drawX + ", DrawY:" + drawY);
        batch.draw(texture, drawX, drawY);
    }
}
