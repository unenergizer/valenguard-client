package com.valenguard.client.screens;

import com.badlogic.gdx.Screen;

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

public enum ScreenType {

    LOADING(new LoadingScreen()),
    LOGIN(new LoginScreen()),
    GAME(new GameScreen());

    private Screen screen;

    ScreenType(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}
