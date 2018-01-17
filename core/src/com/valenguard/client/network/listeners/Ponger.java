package com.valenguard.client.network.listeners;

import com.valenguard.client.network.ServerHandle;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;
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

public class Ponger implements Listener {

    @Opcode(getOpcode = Opcodes.PING_PONG)
    public void onPing(ServerHandle serverHandle) {
           //System.out.println(serverHandle.readString());
           serverHandle.write(Opcodes.PING_PONG, new Write() {
               @Override
               public void accept(ObjectOutputStream outStream) throws IOException {
                   outStream.writeUTF("Pong");
               }
           });
    }
}
