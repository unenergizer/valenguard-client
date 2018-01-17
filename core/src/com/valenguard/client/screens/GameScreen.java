package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.ClientConstants;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.Assets;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.util.AttachableCamera;
import com.valenguard.client.util.ConsoleLogger;
import com.valenguard.client.util.Controller;
import com.valenguard.client.util.GraphicsUtils;

import lombok.Getter;

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

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture playerTexture;
    @Getter
    private Entity player;
    private AttachableCamera camera;
    private ScreenViewport screenViewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void show() {
        System.out.println(ConsoleLogger.INFO.toString() + "Showing the game screen.");

        batch = Valenguard.getInstance().getSpriteBatch();

        // Setup player
        playerTexture = new Texture(Assets.graphics.TEMP_PLAYER_IMG);
        player = new Entity(0, 0);

        // Setup camera
        camera = new AttachableCamera(ClientConstants.SCREEN_WIDTH, ClientConstants.SCREEN_HEIGHT, ClientConstants.ZOOM);
        screenViewport = new ScreenViewport();
        camera.attachEntity(player);

        // Setup Map
        map = new TmxMapLoader().load(Assets.maps.TEST_MAP);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Setup input controls
        Controller inputProcessor = new Controller(player);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        // Update camera
        camera.clampCamera(screenViewport, map);
        camera.update();

        // Render map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Draw sprites
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch, playerTexture);
        batch.end();
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
        System.out.println(ConsoleLogger.INFO.toString() + "Disposing: GameScreen");

        // Dispose of system resources
        playerTexture.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
