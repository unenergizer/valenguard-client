package com.valenguard.client.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.valenguard.client.Valenguard;
import com.valenguard.client.constants.Direction;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.network.shared.Write;

import java.io.IOException;
import java.io.ObjectOutputStream;

import lombok.AllArgsConstructor;
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

    public void move(final Direction direction) {
        Valenguard.getInstance().getClient().getServerHandle().write(Opcodes.MOVE_REQUEST, new Write() {
            @Override
            public void accept(ObjectOutputStream outStream) throws IOException {
                System.out.println("Direction: " + direction.toString() + " - " + direction.getDirection());
                outStream.writeByte(direction.getDirection());
            }
        });
    }
}
