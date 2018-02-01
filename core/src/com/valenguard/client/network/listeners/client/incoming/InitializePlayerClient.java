package com.valenguard.client.network.listeners.client.incoming;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.valenguard.client.Valenguard;
import com.valenguard.client.entities.PlayerClient;
import com.valenguard.client.network.ClientConnection;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.screens.ScreenType;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/27/2018 @ 9:45 PM
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

public class InitializePlayerClient implements Listener {

    private static final String TAG = InitializePlayerClient.class.getSimpleName();

    @Opcode(getOpcode = Opcodes.INIT_PLAYER_CLIENT)
    public void onInitializePlayerClient(final ServerHandler serverHandler) {

        ClientConnection client = Valenguard.getInstance().getClientConnection();

        // TODO: Check for authentication

        Gdx.app.debug(TAG,"Initializing the player");

        // Network connection was successful.
        Gdx.app.debug(TAG,"Connection successful! Switching to the Game screen.");


        // Send the login window a connection message
        client.safeInfoMessage("Connected!", Color.GREEN);

        // Setup the player client entity
        Valenguard.getInstance().setPlayerClient(new PlayerClient(serverHandler.readInt(), serverHandler.readInt(), serverHandler.readInt()));


        // Now switch to the game screen!
        client.safeChangeScreen(ScreenType.GAME);
    }
}
