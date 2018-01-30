package com.valenguard.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.valenguard.client.assets.GameMusic;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.Valenguard;
import com.valenguard.client.assets.FileManager;
import com.valenguard.client.assets.GameTexture;
import com.valenguard.client.assets.GameUI;
import com.valenguard.client.util.GraphicsUtils;

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

@Getter
@Setter
public class LoginScreen extends ScreenAdapter {

    private static final String TAG = LoginScreen.class.getSimpleName();
    
    private SpriteBatch spriteBatch;
    private Stage stage;
    private FileManager fileManager;
    private Skin skin;
    private TextField accountField = null;
    private TextField passwordField = null;
    private Music music = null;

    @Override
    public void show() {
        Gdx.app.debug(TAG, "Showing the login screen.");

        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        fileManager = Valenguard.getInstance().getFileManager();
        Gdx.input.setInputProcessor(stage);

        //Temporary music setup
        //music = Valenguard.getInstance().getFileManager().getMusic(GameMusic.LOGIN_SCREEN_THEME);
        //music.play();

        buildStage("", Color.WHITE);
    }

    public void buildStage(String connectionMessage, Color messageColor) {
        if (stage != null) stage.clear();

        boolean debugStage = false;

        // temporary until asset manager is implemented
        skin = new Skin(Gdx.files.internal(GameUI.UI_SKIN.getFilePath()));

        /**
         * SETUP LOGIN
         */
        Table loginTable = new Table();
        loginTable.setFillParent(true);
        loginTable.setDebug(debugStage);
        loginTable.setColor(Color.RED);
        stage.addActor(loginTable);

        // create login widgets
        fileManager.loadTexture(GameTexture.LOGO_BIG);
        Texture texture = fileManager.getTexture(GameTexture.LOGO_BIG);
        Image logo = new Image(texture);
        Label accountLabel = new Label("Username", skin);
        Label passwordLabel = new Label("Password", skin);
        Label infoMessageLabel = new Label(connectionMessage, skin);
        infoMessageLabel.setColor(messageColor);

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

        // add widgets to table
        loginTable.add(logo).colspan(2).pad(0, 0, 30, 0);
        loginTable.row().pad(10);
        loginTable.add(accountLabel);
        loginTable.add(accountField).uniform();
        loginTable.row().pad(10);
        loginTable.add(passwordLabel);
        loginTable.add(passwordField).uniform();
        loginTable.row().pad(10);
        loginTable.add(loginButton).colspan(2).center().width(150);
        loginTable.row().pad(10);
        loginTable.add(infoMessageLabel).colspan(2).pad(20, 0, 0, 0);


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

        /**
         * SETUP VERSION
         */
        Table versionTable = new Table();
        versionTable.setFillParent(true);
        versionTable.setDebug(debugStage);
        stage.addActor(versionTable);

        // create version widgets
        Label versionLabel = new Label("ClientConnection version " + ClientConstants.GAME_VERSION, skin);
        versionLabel.setFontScale(.8f);

        // show client version in lower left hand corner
        versionTable.add(versionLabel).expand().bottom().left().pad(10);

        /**
         * SETUP COPYRIGHT NOTICE
         */
        Table copyrightTable = new Table();
        copyrightTable.setFillParent(true);
        copyrightTable.setDebug(debugStage);
        stage.addActor(copyrightTable);

        // create copyright widgets
        Label copyrightLabel = new Label("Copyright © 2017-2018 Valenguard MMO. All Rights Reserved.", skin);
        copyrightLabel.setFontScale(.8f);

        // show client version in bottom middle of the screen
        copyrightTable.add(copyrightLabel).expand().center().bottom().pad(10);

        /**
         *  SETUP HELP BUTTONS
         */
        Table buttonTable = new Table();
        buttonTable.setFillParent(false);
        buttonTable.setDebug(debugStage);
        stage.addActor(buttonTable);

        Table buttonTableWrapper = new Table();
        buttonTableWrapper.setFillParent(true);
        buttonTableWrapper.setDebug(debugStage);
        stage.addActor(buttonTableWrapper);

        // create help widgets
        TextButton registerButton = new TextButton("New Account", skin);
        registerButton.pad(3, 10, 3, 10);
        TextButton forgotPasswordButton = new TextButton("Forgot Password", skin);
        forgotPasswordButton.pad(3, 10, 3, 10);
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.pad(3, 10, 3, 10);
        TextButton exitButton = new TextButton("Exit", skin);
        registerButton.pad(3, 10, 3, 10);

        float buttonWidth = 150;

        // show help buttons in lower right hand corner
        buttonTable.add(registerButton).pad(3, 0, 3, 0).width(buttonWidth);
        buttonTable.row();
        buttonTable.add(forgotPasswordButton).pad(3, 0, 3, 0).width(buttonWidth);
        buttonTable.row();
        buttonTable.add(settingsButton).pad(3, 0, 3, 0).width(buttonWidth);
        buttonTable.row();
        buttonTable.add(exitButton).pad(3, 0, 3, 0).width(buttonWidth);

        // add button table to the button table wrapper
        buttonTableWrapper.add(buttonTable).expand().bottom().right().pad(10);

        // opens up web page for player registration
        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI(ClientConstants.WEB_REGISTER);
            }
        });

        // opens up web page to recover lost password
        forgotPasswordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI(ClientConstants.WEB_LOST_PASSWORD);
            }
        });

        // exit the game
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        GraphicsUtils.clearScreen();

        spriteBatch.begin();
        spriteBatch.draw(fileManager.getTexture(GameTexture.LOGIN_BACKGROUND), 0, 0, stage.getWidth(), stage.getHeight());
        spriteBatch.end();

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
        Gdx.app.debug(TAG, "Disposing: LoginScreen");

        skin.dispose();
        stage.dispose();
        spriteBatch.dispose();
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
            Gdx.app.debug(TAG, "Login success!!");
            return;
        }

        if (!isAuthenticated) {
            Gdx.app.debug(TAG, "Incorrect AccountName/Password combination!");
        }

        if (!isVersionCheckPassed) {
            Gdx.app.debug(TAG, "Version mismatch! Please upgrade your game-client!");
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
