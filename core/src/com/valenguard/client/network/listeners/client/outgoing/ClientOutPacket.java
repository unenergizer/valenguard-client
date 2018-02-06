package com.valenguard.client.network.listeners.client.outgoing;

import com.valenguard.client.Valenguard;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Write;

import java.io.IOException;
import java.io.ObjectOutputStream;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/29/2018 @ 9:46 AM
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

public abstract class ClientOutPacket {

    /**
     * Opcode to send with the out-going packet.
     */
    protected byte opcode;

    /**
     * Used to easily send out packets to the server.
     */
    protected ServerHandler serverHandler = Valenguard.getInstance().getClientConnection().getServerHandler();

    public ClientOutPacket(byte opcode) {
        this.opcode = opcode;
    }

    /**
     * Sends the packet to the player.
     */
    public void sendPacket() {
        serverHandler.write(opcode, new Write() {
            @Override
            public void accept(ObjectOutputStream write) throws IOException {
                ClientOutPacket.this.createPacket(write);
            }
        });
    }

    /**
     * Creates the packet.
     */
    protected abstract void createPacket(ObjectOutputStream write) throws IOException;
}
