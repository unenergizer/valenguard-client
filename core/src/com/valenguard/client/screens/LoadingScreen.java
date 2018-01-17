package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.Assets;
import com.valenguard.client.util.ConsoleLogger;
import com.valenguard.client.util.GraphicsUtils;

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

public class LoadingScreen extends ScreenAdapter {

    private AssetManager assetManager;
    private SpriteBatch batch;
    private Stage stage;
    private int currentStage = 0;

    @Override
    public void show() {
        System.out.println(ConsoleLogger.INFO.toString() + "Showing the loading screen.");

        batch = Valenguard.getInstance().getSpriteBatch();
        assetManager = Valenguard.getInstance().getAssetManager();
        stage = new Stage(new ScreenViewport());

        // begin loading quick graphics (for loading screen)
        assetManager.load(Assets.graphics.LOGIN_BACKGROUND, Texture.class);
        assetManager.finishLoading();

        // initialize loading screen graphics
        Valenguard.getInstance().setLoginBackground((Texture) assetManager.get(Assets.graphics.LOGIN_BACKGROUND));
    }

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        // draw background
        batch.begin();
        batch.draw(Valenguard.getInstance().getLoginBackground(), 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();

        // display loading bar here
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Currently loading assets have finished. Either must switch
        // screens and/or start loading in new assets
        if (assetManager.update()) {
            switch (currentStage) {
                case 0:
                    loadAllGraphics();
                    break;
                case 1:
                    loadAllAudio();
                    break;
                case 2:
                    // Looks like were all finished!
                    assetManager.finishLoading();
                    // After going through all asset types switch screens
                    Valenguard.getInstance().setScreen(ScreenType.LOGIN);

                    break;
            }
            currentStage++;
        }

        // Current progress of the currently loading asserts
        float currentProgress = assetManager.getProgress();

        // Use the progress to update the progress bar respectively

    }

    private void loadAllGraphics() {
        // BACKGROUNDS
        //assetManager.load(Assets.graphics.LOGIN_BACKGROUND, TextureComponent.class);

        // ENTITIES
        assetManager.load(Assets.graphics.TEMP_PLAYER_IMG, Texture.class);
    }

    private void loadAllAudio() {
        // TODO: LOAD AUDIO
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        System.out.println(ConsoleLogger.INFO.toString() + "Disposing: LoadingScreen");

        stage.dispose();
    }
}
