package com.valenguard.client.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.valenguard.client.entities.Entity;

/**
 * Created by unene on 12/20/2017.
 */

public class AttachableCamera extends OrthographicCamera {

    private Entity following;

    public AttachableCamera(float width, float height) {
        super.setToOrtho(false, width, height);
    }

    public void attachEntity(Entity following) {
        this.following = following;
    }

    @Override
    public void update() {
        if (following == null) return;
        super.position.x = following.getX();
        super.position.y = following.getY();
        super.update();
    }
}
