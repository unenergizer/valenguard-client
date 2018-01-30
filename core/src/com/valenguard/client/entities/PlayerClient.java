package com.valenguard.client.entities;

import com.badlogic.gdx.Gdx;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/28/2018 @ 4:49 PM
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

public class PlayerClient extends Entity {

    private static final String TAG = PlayerClient.class.getSimpleName();

    public PlayerClient(int entityId, float x, float y) {
        super(entityId, x, y);

        Gdx.app.debug(TAG, "EntityID: " + entityId + ", X: " + x + ", Y: " + y);
    }

}
