package com.valenguard.client.util;

import com.badlogic.gdx.InputProcessor;
import com.valenguard.client.constants.Direction;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.maps.MapData;
import com.valenguard.client.network.listeners.client.outgoing.MoveRequest;

import lombok.AllArgsConstructor;

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

@AllArgsConstructor
public class Controller implements InputProcessor {

    private PlayerClient playerClient;

    public boolean keyDown(int keycode) {
        //  System.out.println("keyDOWN: " + keycode);
        return false;
    }

    public boolean keyUp(int keycode) {
        //  System.out.println("keyUP: " + keycode);
        return false;
    }

    public boolean keyTyped(char character) {

        MapData map = playerClient.getCurrentMap();
        int currentTileX = playerClient.getTileX();
        int currentTileY = playerClient.getTileY();
        switch (character) {
            case 'w':
                if (!map.isTraversable(currentTileX, currentTileY + 1) || playerClient.isMoving()) break;
                System.out.println("starting pre movement.");
                new MoveRequest(Direction.UP).sendPacket();
                break;
            case 's':
                if (!map.isTraversable(currentTileX, currentTileY - 1) || playerClient.isMoving()) break;
                new MoveRequest(Direction.DOWN).sendPacket();
                break;
            case 'a':
                if (!map.isTraversable(currentTileX - 1, currentTileY) || playerClient.isMoving()) break;
                new MoveRequest(Direction.LEFT).sendPacket();
                break;
            case 'd':
                if (!map.isTraversable(currentTileX + 1, currentTileY) || playerClient.isMoving()) break;
                new MoveRequest(Direction.RIGHT).sendPacket();
                break;
        }

        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        // System.out.println("touchDOWN- X: " + x + ", Y: " + y + ", POINTER: " + pointer + ", BUTTON: " + button);
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        // System.out.println("touchUP- X: " + x + ", Y: " + y + ", POINTER: " + pointer + ", BUTTON: " + button);
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        // System.out.println("touchDRAGGED- X: " + x + ", Y: " + y + ", POINTER: " + pointer);
        return false;
    }

    public boolean mouseMoved(int x, int y) {
        // System.out.println("mouseMOVED- X: " + x + ", Y: " + y);
        return false;
    }

    public boolean scrolled(int amount) {
        // System.out.println("scrolled: " + amount);
        return false;
    }
}

