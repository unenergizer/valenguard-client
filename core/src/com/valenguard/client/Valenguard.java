package com.valenguard.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.valenguard.client.assets.FileManager;
import com.valenguard.client.constants.ServerConstants;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.network.ClientConnection;
import com.valenguard.client.network.ClientEventBus;
import com.valenguard.client.network.listeners.client.incoming.EntityExitMap;
import com.valenguard.client.network.listeners.client.incoming.EntityJoinedMap;
import com.valenguard.client.network.listeners.client.incoming.EntityMoveUpdate;
import com.valenguard.client.network.listeners.client.incoming.InitializePlayerClient;
import com.valenguard.client.network.listeners.client.incoming.MoveReply;
import com.valenguard.client.network.listeners.client.incoming.PlayerMapChange;
import com.valenguard.client.screens.GameScreen;
import com.valenguard.client.screens.LoadingScreen;
import com.valenguard.client.screens.LoginScreen;
import com.valenguard.client.screens.ScreenType;
import com.valenguard.client.util.Consumer;

import lombok.Getter;
import lombok.Setter;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-clientConnection
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

    private static final String TAG = Valenguard.class.getSimpleName();

    private static Valenguard valenguard;
    private GameScreen gameScreen;
    private LoadingScreen loadingScreen;
    private LoginScreen loginScreen;
    private ScreenType screenType;
    private FileManager fileManager;
    private ClientConnection clientConnection;
    @Setter
    private volatile boolean canUseLoginButton = true;
    @Setter
    private volatile boolean connectedToServer = false;
    @Setter
    private PlayerClient playerClient;

    protected Valenguard() {
    }

    public static Valenguard getInstance() {
        if (valenguard == null) valenguard = new Valenguard();
        return valenguard;
    }

    @Override
    public void create() {
        Gdx.app.debug(TAG, "Starting game...");

        // startup needed classes
        fileManager = new FileManager();
        clientConnection = new ClientConnection();

        // load screens
        gameScreen = new GameScreen();
        loadingScreen = new LoadingScreen();
        loginScreen = new LoginScreen();

        // set the first screen
        screenType = ScreenType.LOADING;
        setScreen(loadingScreen);
    }

    /**
     * Displays the screen that will be shown to the player.
     *
     * @param screenType The type of screen we want to display.
     */
    public void setScreen(ScreenType screenType) {

        // Make sure we aren't setting the screen to the same screen.
        if (this.screenType == screenType) {
            Gdx.app.debug(TAG,"Trying to change screens, but we are already on requested screen.");
            return;
        }

        // Change screens.
        switch (screenType) {
            case LOADING:
                this.screenType = screenType;
                setScreen(loadingScreen);
                break;
            case LOGIN:
                this.screenType = screenType;
                setScreen(loginScreen);
                break;
            case GAME:
                this.screenType = screenType;
                setScreen(gameScreen);
                break;
        }
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "Closing game...");
        Gdx.app.debug(TAG, "Disposing: Valenguard Game");

        // dispose assets
        fileManager.dispose();

        // dispose screens
        gameScreen.dispose();
        loginScreen.dispose();
        loadingScreen.dispose();
    }

    /**
     * Initialize connection to the game server.
     */
    public void initializeNetwork() {
        canUseLoginButton = false;
        clientConnection.openConnection(
                ServerConstants.SERVER_ADDRESS,
                (short) ServerConstants.SERVER_PORT,
                new Consumer<ClientEventBus>() {
                    @Override
                    public void accept(ClientEventBus clientEventBus) {
                        clientEventBus.registerListener(new MoveReply());
                        clientEventBus.registerListener(new EntityMoveUpdate());
                        clientEventBus.registerListener(new InitializePlayerClient());
                        clientEventBus.registerListener(new EntityJoinedMap());
                        clientEventBus.registerListener(new EntityExitMap());
                        clientEventBus.registerListener(new PlayerMapChange());
                    }
                });
    }
}
