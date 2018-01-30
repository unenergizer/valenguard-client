package com.valenguard.client.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.valenguard.client.constants.ClientConstants;
import com.valenguard.client.entities.Entity;

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

public class AttachableCamera extends OrthographicCamera {

    private Entity following;

    public AttachableCamera(float width, float height, float zoom) {
        super.setToOrtho(false, width, height);
        super.update();
        this.zoom = zoom;
    }

    public void attachEntity(Entity following) {
        this.following = following;
    }

    public void clampCamera(Viewport screenViewport, TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float cameraMinX = (screenViewport.getScreenWidth() / 2) * zoom;
        float cameraMinY = (screenViewport.getScreenHeight() / 2) * zoom;
        float cameraMaxX = layer.getWidth() * layer.getTileWidth() - cameraMinX;
        float cameraMaxY = layer.getHeight() * layer.getTileHeight() - cameraMinY;

        position.x = MathUtils.clamp(following.getX() * ClientConstants.TILE_SIZE, cameraMinX, cameraMaxX);
        position.y = MathUtils.clamp(following.getY() * ClientConstants.TILE_SIZE, cameraMinY, cameraMaxY);
    }
}
