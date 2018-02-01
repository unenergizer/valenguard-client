package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.FileManager;
import com.valenguard.client.assets.GameMap;
import com.valenguard.client.assets.GameTexture;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.util.AttachableCamera;
import com.valenguard.client.util.Controller;
import com.valenguard.client.util.GraphicsUtils;
import com.valenguard.client.util.Timer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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

public class GameScreen implements Screen {

    private static final String TAG = GameScreen.class.getSimpleName();

    @Getter
    @Setter
    private Queue<Entity> entityList = new ConcurrentLinkedQueue<Entity>();

    /**
     * Used to perform timing operating in relation to the
     * render method.
     */
    @Getter
    @Setter
    private Queue<Timer> timers = new ConcurrentLinkedQueue<Timer>();

    private FileManager fileManager;
    private SpriteBatch spriteBatch;
    private Texture playerTexture;
    @Getter
    private AttachableCamera camera;
    private ScreenViewport screenViewport;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;


    //https://stackoverflow.com/questions/28113700/libgdx-render-and-update-calling-speed

    static final double DT = 1/60.0;
    static final int MAX_UPDATES_PER_FRAME = 3; //for preventing spiral of death
    private long currentTimeMillis;





    @Override
    public void show() {

        currentTimeMillis = System.currentTimeMillis();

        Gdx.app.debug(TAG, "Showing the game screen.");

        spriteBatch = new SpriteBatch();
        fileManager = Valenguard.getInstance().getFileManager();

        // Setup player
        playerTexture = fileManager.getTexture(GameTexture.TEMP_PLAYER_IMG);

        // Setup camera
        camera = new AttachableCamera(ClientConstants.SCREEN_WIDTH, ClientConstants.SCREEN_HEIGHT, ClientConstants.ZOOM);
        screenViewport = new ScreenViewport();
        camera.attachEntity(Valenguard.getInstance().getPlayerClient());

        // Setup Map
        setTiledMap(GameMap.MAIN_TOWN);

        // Setup input controls
        Controller inputProcessor = new Controller(Valenguard.getInstance().getPlayerClient());
        Gdx.input.setInputProcessor(inputProcessor);
    }

    int iiii = 0;

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        long newTimeMillis = System.currentTimeMillis();
        float frameTimeSeconds = (newTimeMillis - currentTimeMillis) / 1000f;
        currentTimeMillis = newTimeMillis;

        while (frameTimeSeconds > 0f) {
            double deltaTimeSeconds = Math.min(frameTimeSeconds, DT);
            update();
            frameTimeSeconds -= deltaTimeSeconds;
        }

        // Update camera
        camera.clampCamera(screenViewport, tiledMap);
        camera.update();

        // Render tiledMap
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Draw sprites
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Draw all other entities
        if (!entityList.isEmpty()) {
            for (Entity entity : entityList) {
                entity.draw(spriteBatch, fileManager.getTexture(GameTexture.TEMP_OTHER_PLAYER_IMG));
            }
        }

        // Draw our main player
        Valenguard.getInstance().getPlayerClient().draw(spriteBatch, playerTexture);

        spriteBatch.end();
    }

    private void update() {
        // Running timers
        for (Timer timer : timers) {

            // Handling callbacks that happen after a period of time.
            if (timer.getRunLaterCallback() != null) {
                // The period for the timer has ended.
                if (timer.incrementTime()) {
                    timer.getRunLaterCallback().run();
                    timers.remove(timer);
                    timer.setCanceled(true);
                }
            }

            // Hanlding callbacks that are called each tick for a period.
            if (timer.getRunForPeriodCallback() != null) {
                timer.getRunForPeriodCallback().accept(timer.getMillisecondsPassed());
                // The period for the timer has ended.
                if (timer.incrementTime()) {
                    timers.remove();
                    timer.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height, true);
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "Disposing: GameScreen");

        // Dispose of system resources
        if (mapRenderer != null) mapRenderer.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
    }

    /**
     * Sets the tiled map to be rendered.
     *
     * @param gameMap The tiled map based on name
     */
    public void setTiledMap(GameMap gameMap) {
        fileManager.loadTiledMap(gameMap);
        tiledMap = fileManager.getTiledMap(gameMap);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }
}
