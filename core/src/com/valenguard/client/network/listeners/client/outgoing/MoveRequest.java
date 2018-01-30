package com.valenguard.client.network.listeners.client.outgoing;

import com.badlogic.gdx.Gdx;
import com.valenguard.client.constants.Direction;
import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.network.shared.Write;

import java.io.IOException;
import java.io.ObjectOutputStream;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/8/2018 @ 5:28 PM
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

public class MoveRequest extends ClientOutPacket {

    private static final String TAG = MoveRequest.class.getSimpleName();

    private Direction direction;

    public MoveRequest(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void sendPacket() {
        serverHandler.write(Opcodes.MOVE_REQUEST, new Write() {
            @Override
            public void accept(ObjectOutputStream outStream) throws IOException {
                //Gdx.app.debug(TAG, "MoveRequest: " + direction.toString() + " = " + direction.getDirection());
                outStream.writeByte(direction.getDirection());
            }
        });
    }
}
