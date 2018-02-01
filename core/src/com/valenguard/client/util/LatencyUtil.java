package com.valenguard.client.util;

import com.valenguard.client.network.listeners.client.outgoing.PingPacket;

import lombok.Getter;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 2/1/2018 @ 1:23 PM
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

public class LatencyUtil {

    private long millisecondsSentTime;
    private volatile boolean running = false;
    @Getter
    private long ping;

    public void handleLatency(long millisecondsTimeOfArr) {
        ping = millisecondsTimeOfArr - millisecondsSentTime;
    }

    public void sendPingPacket() {
        millisecondsSentTime = System.currentTimeMillis();
        new PingPacket().sendPacket();
    }
}
