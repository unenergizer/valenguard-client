package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.ClientConstants;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.Assets;
import com.valenguard.client.util.ConsoleLogger;
import com.valenguard.client.util.GraphicsUtils;

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
@Setter
public class LoginScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private Stage stage;
    private AssetManager assetManager;
    private Skin skin;
    private TextField accountField = null;
    private TextField passwordField = null;

    @Override
    public void show() {
        System.out.println(ConsoleLogger.INFO.toString() + "Showing the login screen.");

        batch = Valenguard.getInstance().getSpriteBatch();
        stage = new Stage(new ScreenViewport());
        assetManager = Valenguard.getInstance().getAssetManager();
        Gdx.input.setInputProcessor(stage);

        buildStage("", Color.WHITE);
    }

    public void buildStage(String connectionMessage, Color messageColor) {
        if (stage != null) stage.clear();

        // setup main display table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.setColor(Color.RED);
        stage.addActor(table);

        // setup the table for the buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(false);
        buttonTable.setDebug(false);
        stage.addActor(buttonTable);

        // setup the table for the buttons
        Table versionTable = new Table();
        versionTable.setFillParent(true);
        versionTable.setDebug(false);
        stage.addActor(versionTable);

        // setup the table for the buttons
        Table copyrightTable = new Table();
        copyrightTable.setFillParent(true);
        copyrightTable.setDebug(false);
        stage.addActor(copyrightTable);

        // temporary until asset manager is implemented
        skin = new Skin(Gdx.files.internal(Assets.userInterface.UI_SKIN));

        // create widgets
        Label nameLabel = new Label("Valenguard MMO", skin);
        nameLabel.setFontScale(2);
        Label accountLabel = new Label("Username", skin);
        Label passwordLabel = new Label("Password", skin);
        Label infoMessageLabel = new Label(connectionMessage, skin);
        infoMessageLabel.setColor(messageColor);
        Label versionLabel = new Label("Client version " + ClientConstants.GAME_VERSION, skin);
        versionLabel.setFontScale(.8f);
        Label copyrightLabel = new Label("Copyright © 2017-2018 Valenguard MMO. All Rights Reserved.", skin);
        copyrightLabel.setFontScale(.8f);

        // If the account field existed before a stage rebuild, keep its contents.
        // Basically if the player tries to connect but fails, keep their login id.
        if (accountField == null) {
            accountField = new TextField(null, skin);
            accountField.setFocusTraversal(false);
            accountField.setMaxLength(12);
        }

        passwordField = new TextField(null, skin);
        passwordField.setFocusTraversal(false);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('#');
        passwordField.setMaxLength(16);

        TextButton loginButton = new TextButton("Login", skin);
        loginButton.pad(3, 10, 3, 10);

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.pad(3, 10, 3, 10);

        // add buttons to button table
        buttonTable.add(loginButton).pad(10).width(100);
        buttonTable.add(registerButton).pad(10).width(100);

        // show client version in lower left hand corner
        versionTable.add(versionLabel).expand().bottom().left().pad(10);

        // show client version in lower left hand corner
        copyrightTable.add(copyrightLabel).expand().bottom().right().pad(10);

        // add widgets to table
        table.add(nameLabel).colspan(2).pad(0, 0, 30, 0);
        table.row().pad(10);
        table.add(accountLabel);
        table.add(accountField).uniform();
        table.row().pad(10);
        table.add(passwordLabel);
        table.add(passwordField).uniform();
        table.row().pad(10);
        table.add(buttonTable).colspan(2).center();
        table.row().pad(10);
        table.add(infoMessageLabel).colspan(2).pad(20, 0, 0, 0);


        // setup event listeners
        accountField.setTextFieldListener(new AccountInput());
        passwordField.setTextFieldListener(new PasswordInput());

        // login to network
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                attemptLogin();
            }
        });

        // opens up web page for player registration
        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI(ClientConstants.WEB_REGISTER);
            }
        });
    }

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        batch.begin();
        batch.draw(Valenguard.getInstance().getLoginBackground(), 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println(ConsoleLogger.INFO.toString() + "Disposing: LoginScreen");

        skin.dispose();
        stage.dispose();
    }

    /**
     * This will start Netty and attempt to login to the network.
     */
    private void attemptLogin() {
        Gdx.input.setOnscreenKeyboardVisible(false); // closeConnection the android keyboard
//        loginInfo = new LoginInfo(
//                accountField.getText(),
//                passwordField.getText(),
//                Constants.GAME_VERSION);

        // Start our network connection.
        //valenguard.startNetty();

        // Clear password filed.
        passwordField.setText("");

        //TODO: SKIP THIS AND GO TO GAME SCREEN.
        if (Valenguard.getInstance().isCanUseLoginButton() && !Valenguard.getInstance().isConnectedToServer()) {
            Valenguard.getInstance().initializeNetwork();
        }
    }

    /**
     * If the network is up and a login error occurs, we will handle those messages here.
     *
     * @param isAuthenticated      True if the accountName/password combo is accurate.
     * @param isVersionCheckPassed True if the game-client version matches the server version.
     */
    public void showLoginInfo(boolean isAuthenticated, boolean isVersionCheckPassed) {
        if (isAuthenticated && isVersionCheckPassed) {
            System.out.println("Login success!!");
            return;
        }

        if (!isAuthenticated) {
            System.out.println("Incorrect AccountName/Password combination!");
        }

        if (!isVersionCheckPassed) {
            System.out.println("Version mismatch! Please upgrade your game-client!");
        }
    }

    /*****************************************************************
     * !!! TEXT FIELD LISTENERS !!!
     *
     * WARNING!!! Watch for following characters...
     * Backspace = \b
     * Enter = \n
     * Tab = \t
     */

    private class AccountInput implements TextField.TextFieldListener {
        @Override
        public void keyTyped(TextField textField, char c) {
            // user hit enter/tab/etc, lets move to next text field
            if (c == '\n' || c == '\r' || c == '\t') {
                stage.setKeyboardFocus(passwordField);
            }
        }
    }

    private class PasswordInput implements TextField.TextFieldListener {
        @Override
        public void keyTyped(TextField textField, char c) {
            if (c == '\t') return; // cancel tab
            if (c == '\n' || c == '\r') { // user hit enter, try login
                attemptLogin();
            }
        }
    }
}
