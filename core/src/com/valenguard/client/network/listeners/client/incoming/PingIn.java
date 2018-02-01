package com.valenguard.client.network.listeners.client.incoming;

import com.valenguard.client.Valenguard;
import com.valenguard.client.network.ServerHandler;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;
import com.valenguard.client.network.shared.Opcodes;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 2/1/2018 @ 1:19 PM
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

public class PingIn implements Listener {

    @Opcode(getOpcode = Opcodes.PING)
    public void onIncomingPing(ServerHandler serverHandler) {
        long millisecondsTimeOfArr = System.currentTimeMillis();
        Valenguard.getInstance().getGameScreen().getLatencyUtil().handleLatency(millisecondsTimeOfArr);
    }
}
