package com.valenguard.client.util;

import com.valenguard.client.Valenguard;

import lombok.Getter;
import lombok.Setter;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/27/2018 @ 8:51 PM
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

public class Timer {

    public static final int SECOND = 60;
    public static final int MINUTE = SECOND * 60;

    @Getter
    private int millisecondsForRun;

    @Getter
    @Setter
    private int millisecondsPassed;

    @Getter
    private Runnable runLaterCallback;

    @Getter
    private Consumer<Integer> runForPeriodCallback;

    @Getter
    @Setter
    boolean isCanceled = false;

    /**
     * Runs after a period of time.
     *
     * @param callback The callback to be called after the time has passsed.
     * @param milliseconds The time in milliseconds until the callback should be called.
     * @return
     */
    public Timer runLater(Runnable callback, int milliseconds) {
        runLaterCallback = callback;
        millisecondsForRun = milliseconds;
        return this;
    }

    /**
     * Runs for a period of time.
     *
     * @param callback
     * @param milliseconds
     * @return
     */
    public Timer runForPeriod(Consumer<Integer> callback, int milliseconds) {
        runForPeriodCallback = callback;
        millisecondsForRun = milliseconds;
        return this;
    }

    public Timer start() {
        Valenguard.getInstance().getGameScreen().getTimers().add(this);
        return this;
    }

    public void cancel() {
        Valenguard.getInstance().getGameScreen().getTimers().remove(this);
        isCanceled = true;
    }

    public boolean incrementTime() {
        millisecondsPassed++;
        return millisecondsPassed == millisecondsForRun;
    }
}
