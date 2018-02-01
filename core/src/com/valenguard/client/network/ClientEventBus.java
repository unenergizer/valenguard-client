package com.valenguard.client.network;


import com.badlogic.gdx.Gdx;
import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 12/20/2017 @ 12:12 AM
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

public class ClientEventBus {
    private static final String TAG = ClientEventBus.class.getSimpleName();

    private final Map<Byte, CallbackData> listeners = new HashMap<Byte, CallbackData>();

    @AllArgsConstructor
    private class CallbackData {
        private Listener listener;
        private Method method;
    }

    public void registerListener(Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            for (Annotation opcodeAnno : method.getAnnotations()) {
                if (!opcodeAnno.annotationType().equals(Opcode.class)) continue;
                Class<?>[] params = method.getParameterTypes();
                String error = "Listener: " + listener;
                if (params.length != 1)
                    throw new RuntimeException(error + " must have 1 parameter.");
                if (!params[0].equals(ServerHandler.class))
                    throw new RuntimeException(error + " first parameter must be of type ServerHandle.");
                listeners.put(((Opcode) opcodeAnno).getOpcode(), new CallbackData(listener, method));
            }
        }
    }

    public void publish(byte opcode, ServerHandler serverHandler) {
        System.out.println("PUBLISHING OPCODE: " + opcode);
        CallbackData callbackData = listeners.get(opcode);
        if (callbackData == null) {
            Gdx.app.debug(TAG, "Callback data was null for " + opcode);
            return;
        }
        try {
            callbackData.method.invoke(callbackData.listener, serverHandler);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
