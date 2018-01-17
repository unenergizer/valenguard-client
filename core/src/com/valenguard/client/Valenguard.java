package com.valenguard.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.valenguard.client.network.listeners.Ponger;
import com.valenguard.client.network.listeners.incoming.MoveReply;
import com.valenguard.client.network.listeners.outgoing.MoveRequest;
import com.valenguard.client.screens.ScreenType;
import com.valenguard.client.network.Client;
import com.valenguard.client.network.ClientEventBus;
import com.valenguard.client.util.ConsoleLogger;
import com.valenguard.client.util.Consumer;

import lombok.Getter;
import lombok.Setter;

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

@Getter
public class Valenguard extends Game {

    private static Valenguard valenguard;
    private AssetManager assetManager;
    private SpriteBatch spriteBatch;
    private Client client;
    private Screen screen;
    private ScreenType screenType;
    @Setter
    private Texture loginBackground;
    @Setter
    private volatile boolean canUseLoginButton = true;
    @Setter
    private volatile boolean connectedToServer = false;

    protected Valenguard() {}

    public static Valenguard getInstance() {
        if (valenguard == null) valenguard = new Valenguard();
        return valenguard;
    }

    @Override
    public void create() {
        System.out.println(ConsoleLogger.INFO + "Starting game...");
        assetManager = new AssetManager();
        spriteBatch = new SpriteBatch();
        client = new Client();
        setScreen(ScreenType.LOADING.getScreen());
    }

    /**
     * Displays the screen that will be shown to the player.
     * @param screenType The type of screen we want to display.
     */
    public void setScreen(ScreenType screenType) {
        // Set reference for later
        screen = screenType.getScreen();
        this.screenType = screenType;

        // Set screen with LibGDX
        setScreen(screen);
    }

    @Override
    public void dispose() {
        System.out.println(ConsoleLogger.INFO + "Closing game...");
        System.out.println(ConsoleLogger.INFO.toString() + "Disposing: Valenguard Game");

        assetManager.dispose();
        spriteBatch.dispose();
        screen.dispose();
        loginBackground.dispose();
    }

    /**
     * Initialize connection to the game server.
     */
    public void initializeNetwork() {
        canUseLoginButton = false;
        client.openConnection(
                ServerConstants.SERVER_ADDRESS,
                (short) ServerConstants.SERVER_PORT,
                new Consumer<ClientEventBus>() {
                    @Override
                    public void accept(ClientEventBus clientEventBus) {
                        clientEventBus.registerListener(new Ponger());
                        clientEventBus.registerListener(new MoveReply());
                        clientEventBus.registerListener(new MoveRequest());
                    }
                });
    }
}
