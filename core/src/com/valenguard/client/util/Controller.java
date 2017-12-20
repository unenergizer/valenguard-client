package com.valenguard.client.util;

import com.badlogic.gdx.InputProcessor;
import com.valenguard.client.entities.Entity;

import lombok.AllArgsConstructor;

/**
 * Created by unene on 12/20/2017.
 */

@AllArgsConstructor
public class Controller implements InputProcessor {

    private Entity player;

    public boolean keyDown(int keycode) {
        System.out.println("keyDOWN: " + keycode);
        return false;
    }

    public boolean keyUp(int keycode) {
        System.out.println("keyUP: " + keycode);
        return false;
    }

    public boolean keyTyped(char character) {

        switch (character) {
            case 'w':
                player.move(0, 1);
                break;
            case 's':
                player.move(0, -1);
                break;
            case 'a':
                player.move(-1, 0);
                break;
            case 'd':
                player.move(1, 0);
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

