package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.FileManager;
import com.valenguard.client.assets.GameMap;
import com.valenguard.client.assets.GameTexture;
import com.valenguard.client.assets.GameUI;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.entities.Entity;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.maps.MapData;
import com.valenguard.client.util.AttachableCamera;
import com.valenguard.client.util.Controller;
import com.valenguard.client.util.GraphicsUtils;
import com.valenguard.client.util.LatencyUtil;
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
    @Getter
    private LatencyUtil latencyUtil;

    // TODO: MOVE THIS LATER
    private Texture playerTexture;
    @Getter
    private AttachableCamera camera;
    private ScreenViewport screenViewport;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Skin skin;
    private Stage stage;

    private long ticksPassed = 0;

    @Override
    public void show() {

        Gdx.app.debug(TAG, "Showing the game screen.");

        spriteBatch = new SpriteBatch();
        fileManager = Valenguard.getInstance().getFileManager();
        latencyUtil = new LatencyUtil();

        // Setup player
        playerTexture = fileManager.getTexture(GameTexture.TEMP_PLAYER_IMG);
        PlayerClient playerClient = Valenguard.getInstance().getPlayerClient();

        // Setup camera
        camera = new AttachableCamera(ClientConstants.SCREEN_WIDTH, ClientConstants.SCREEN_HEIGHT, ClientConstants.ZOOM);
        screenViewport = new ScreenViewport();
        camera.attachEntity(playerClient);

        stage = new Stage(screenViewport);
        skin = new Skin(Gdx.files.internal(GameUI.UI_SKIN.getFilePath()));
        buildUI();

        // Setup Map
        setTiledMap(GameMap.MAIN_TOWN);
        playerClient.setCurrentMap(Valenguard.getInstance().getMapManager().getMapData(GameMap.MAIN_TOWN.getMapName()));

        // Setup input controls
        Controller inputProcessor = new Controller(playerClient);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    private void buildUI() {
        if (stage != null) stage.clear();

        boolean debugStage = false;

        Table wrapperTable = new Table();
        wrapperTable.setFillParent(true);
        wrapperTable.setDebug(debugStage);
        stage.addActor(wrapperTable);

        Table infoTable = new Table();
        infoTable.setFillParent(false);
        infoTable.setDebug(debugStage);
        stage.addActor(infoTable);

        // create version widgets
        Label delta = new Label("DeltaTime: " + Math.round(Gdx.graphics.getDeltaTime() * 100000.0) / 100000.0, skin);
        Label fps = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), skin);
        Label ms = new Label("MS: " + latencyUtil.getPing(), skin);
        Label uuid = new Label("UUID: " + Valenguard.getInstance().getPlayerClient().getEntityId(), skin);
        Label tileX = new Label("TileX: " + Valenguard.getInstance().getPlayerClient().getTileX(), skin);
        Label tileY = new Label("TileY: " + Valenguard.getInstance().getPlayerClient().getTileY(), skin);
        Label drawX = new Label("DrawX: " + Valenguard.getInstance().getPlayerClient().getDrawX(), skin);
        Label drawY = new Label("DrawY: " + Valenguard.getInstance().getPlayerClient().getDrawY(), skin);

        wrapperTable.add(infoTable).expand().left().top().pad(10);

        // show client version in lower left hand corner
        infoTable.add(delta);
        infoTable.row();
        infoTable.add(fps).left();
        infoTable.row();
        infoTable.add(ms).left();
        infoTable.row();
        infoTable.add(uuid).left();
        infoTable.row();
        infoTable.add(tileX).left();
        infoTable.row();
        infoTable.add(tileY).left();
        infoTable.row();
        infoTable.add(drawX).left();
        infoTable.row();
        infoTable.add(drawY).left();
    }

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        update();

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

        buildUI();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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

        // Send a ping packet once per second
        if (ticksPassed % Timer.SECOND == 0) {
            latencyUtil.sendPingPacket();
        }

        ticksPassed++;
    }

    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height, true);
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
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
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
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
