package com.valenguard.client.util;

import com.badlogic.gdx.InputProcessor;
import com.valenguard.client.constants.Direction;
import com.valenguard.client.entities.Entity;

import lombok.AllArgsConstructor;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
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

    private Entity player;

    public boolean keyDown(int keycode) {
      //  System.out.println("keyDOWN: " + keycode);
        return false;
    }

    public boolean keyUp(int keycode) {
      //  System.out.println("keyUP: " + keycode);
        return false;
    }

    public boolean keyTyped(char character) {

        switch (character) {
            case 'w':
                player.move(Direction.UP);
                break;
            case 's':
                player.move(Direction.DOWN);
                break;
            case 'a':
                player.move(Direction.LEFT);
                break;
            case 'd':
                player.move(Direction.RIGHT);
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

