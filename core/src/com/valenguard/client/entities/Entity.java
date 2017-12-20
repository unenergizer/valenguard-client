package com.valenguard.client.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by unene on 12/20/2017.
 */

@AllArgsConstructor
@Setter
@Getter
public class Entity {
    private float x, y;

    public void draw(Batch batch, Texture texture) {
        batch.draw(texture, x, y);
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }
}
