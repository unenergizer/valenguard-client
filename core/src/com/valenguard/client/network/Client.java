package com.valenguard.client.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.valenguard.client.Valenguard;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.network.shared.Write;
import com.valenguard.client.screens.LoginScreen;
import com.valenguard.client.screens.ScreenType;
import com.valenguard.client.util.ConsoleLogger;
import com.valenguard.client.util.Consumer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

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

public class Client {

    private final int SECONDS_TO_TIMEOUT = 5;
    @Getter
    private ServerHandle serverHandle;
    private final ClientEventBus eventBus = new ClientEventBus();


    public void openConnection(final String address, final short port, final Consumer<ClientEventBus> registerListeners) {
        System.out.println(ConsoleLogger.NETWORK + "Attempting network connection...");
        safeInfoMessage("Attempting network connection...", Color.YELLOW);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;

                try {

                    socket = new Socket();
                    socket.connect(new InetSocketAddress(address, port), 1000 * SECONDS_TO_TIMEOUT);

                } catch (IOException e) {

                    // Failed to openConnection
                    if (e instanceof ConnectException) {
                        System.out.println(ConsoleLogger.NETWORK + "Failed to openConnection to server!");
                        safeInfoMessage("Failed to connect!", Color.RED);

                        // Allow client to use login button again.
                        Valenguard.getInstance().setCanUseLoginButton(true);

                        return;
                    } else {
                        e.printStackTrace();
                    }
                }

                // Network connection was successful.
                System.out.println(ConsoleLogger.NETWORK + "Connection successful! Switching to the Game screen.");
                safeInfoMessage("Connected!", Color.GREEN);

                // Run the following code in a LibGDX thread.
                // Now switch to the game screen!
                safeChangeScreen(ScreenType.GAME);

                // Update valenguard main instance that a connection has been made.
                Valenguard.getInstance().setConnectedToServer(true);


                registerListeners.accept(eventBus);

                receivePackets(socket);

//                // Sending an initial pong to the server
//                send(Opcodes.PING_PONG, new Write() {
//                    @Override
//                    public void accept(ObjectOutputStream outStream) throws IOException {
//                        outStream.writeUTF("Pong");
//                    }
//                });
            }
        }, "Connection").start();
    }

    private void receivePackets(Socket socket) {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        // Try to establish a input and output streams.
        try {

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Create our server handler
        serverHandle = new ServerHandle(this, socket, inputStream, outputStream);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Valenguard.getInstance().isConnectedToServer()) {
                    try {

                        eventBus.publish(serverHandle.getInputStream().readChar(), serverHandle);

                    } catch (IOException e) {
                        // Socket closed
                        if (!(e instanceof SocketException && !Valenguard.getInstance().isConnectedToServer())) {

                            closeConnection();
                            break;
                        }
                    }
                }
            }
        }, "receive_packets").start();
    }

    /**
     * Safely closes a network connection.
     */
    private void closeConnection() {
        System.out.println(ConsoleLogger.NETWORK + "Closing network connection.");

        Valenguard.getInstance().setConnectedToServer(false);
        serverHandle.closeConnection();

        // Change screens if needed.
        if (Valenguard.getInstance().getScreenType() == ScreenType.GAME) {
            // Change to the Login Screen and send the player an error message.
            safeChangeScreen(ScreenType.LOGIN, "Connection lost!", Color.RED);

            // Allow the player to use the login button again.
            Valenguard.getInstance().setCanUseLoginButton(true);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // Double ensuring that the network is closed
        closeConnection();
    }

    /**
     * Changes the game clients screen within a LibGDX thread.
     *
     * @param screenType The screen we want to change to.
     */
    private void safeChangeScreen(final ScreenType screenType) {
        safeChangeScreen(screenType, "", Color.WHITE);
    }

    /**
     * Changes the game clients screen within a LibGDX thread.
     *
     * @param screenType  The screen we want to change to.
     * @param infoMessage A message to send after the screen change.
     * @param color       The color of the message to send.
     */
    private void safeChangeScreen(final ScreenType screenType, final String infoMessage, final Color color) {
        if (screenType == Valenguard.getInstance().getScreenType()) {
            System.out.println(ConsoleLogger.ERROR.toString() +
                    "Trying to change screens, but we are already on requested screen.");
            return;
        }

        // Run the following code in a LibGDX thread.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Valenguard.getInstance().setScreen(screenType);

                // Send Login Screen info message if applicable.
                if (screenType == ScreenType.LOGIN) {
                    LoginScreen screen = (LoginScreen) Valenguard.getInstance().getScreen();
                    screen.buildStage(infoMessage, color);
                }
            }
        });
    }

    /**
     * Sends a info message to our login screen if we are currently on it.
     *
     * @param infoMessage The message we want to send.
     * @param color       The color of the message we are sending.
     */
    private void safeInfoMessage(final String infoMessage, final Color color) {
        // Make sure we only send messages to a proper login screen.
        if (Valenguard.getInstance().getScreenType() != ScreenType.LOGIN) {
            System.out.println(ConsoleLogger.ERROR.toString() +
                    "Trying to send a Login Screen info message on a different screen.");
            return;
        }

        // Run the following code in a LibGDX thread.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                LoginScreen screen = (LoginScreen) Valenguard.getInstance().getScreen();
                screen.buildStage(infoMessage, color);
            }
        });
    }
}
