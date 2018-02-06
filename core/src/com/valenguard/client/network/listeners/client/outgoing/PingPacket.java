package com.valenguard.client.network.listeners.client.outgoing;

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
 * Original File Date: 2/1/2018 @ 1:31 PM
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

public class PingPacket extends ClientOutPacket {

    public PingPacket() {
        super(Opcodes.PING);
    }

    @Override
    protected void createPacket(ObjectOutputStream write) throws IOException {
        //Nothing to write. Sending opcode only.
    }
}
