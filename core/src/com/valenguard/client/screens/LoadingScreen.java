package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.FileManager;
import com.valenguard.client.assets.GameMusic;
import com.valenguard.client.assets.GameTexture;
import com.valenguard.client.util.GraphicsUtils;

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

public class LoadingScreen extends ScreenAdapter {

    private static final String TAG = LoginScreen.class.getSimpleName();
    
    private FileManager fileManager;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private int currentStage = 0;

    @Override
    public void show() {
        Gdx.app.debug(TAG, "Showing the loading screen.");

        spriteBatch = new SpriteBatch();
        fileManager = Valenguard.getInstance().getFileManager();
        stage = new Stage(new ScreenViewport());

        // begin loading quick graphics (for loading screen)
        fileManager.loadTexture(GameTexture.LOGIN_BACKGROUND);
    }

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        // draw background
        spriteBatch.begin();
        spriteBatch.draw(fileManager.getTexture(GameTexture.LOGIN_BACKGROUND), 0, 0, stage.getWidth(), stage.getHeight());
        spriteBatch.end();

        // display loading bar here
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Currently loading assets have finished. Either must switch
        // screens and/or start loading in new assets
        if (fileManager.updateAssetLoading()) {
            switch (currentStage) {
                case 0:
                    loadAllGraphics();
                    break;
                case 1:
                    loadAllAudio();
                    break;
                case 2:
                    // After going through all asset types switch screens
                    Valenguard.getInstance().setScreen(ScreenType.LOGIN);

                    break;
            }
            currentStage++;
        }

        // Current progress of the currently loading asserts
        float currentProgress = fileManager.loadCompleted();

        // Use the progress to update the progress bar respectively

    }

    private void loadAllGraphics() {
        // BACKGROUNDS
        //assetManager.load(Assets.graphics.LOGIN_BACKGROUND, TextureComponent.class);

        // ENTITIES
        fileManager.loadTexture(GameTexture.TEMP_PLAYER_IMG);
        fileManager.loadTexture(GameTexture.TEMP_OTHER_PLAYER_IMG);
    }

    private void loadAllAudio() {
        fileManager.loadMusic(GameMusic.LOGIN_SCREEN_THEME);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "Disposing: LoadingScreen");

        stage.dispose();
        spriteBatch.dispose();
    }
}
