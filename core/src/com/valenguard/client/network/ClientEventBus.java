package com.valenguard.client.network;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


import com.valenguard.client.network.shared.Listener;
import com.valenguard.client.network.shared.Opcode;

import lombok.AllArgsConstructor;

/**
 * Created by unene on 12/20/2017.
 */

public class ClientEventBus {

    private Map<Character, CallbackData> listeners = new HashMap<Character, CallbackData>();

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
                if (!params[0].equals(ServerHandle.class))
                    throw new RuntimeException(error + " first parameter must be of type ServerHandle.");
                listeners.put(((Opcode) opcodeAnno).getOpcode(), new CallbackData(listener, method));
            }
        }
    }

    public void publish(char opcode, ServerHandle serverHandle) {
        CallbackData callbackData = listeners.get(opcode);
        if (callbackData == null) {
            System.out.printf("callback data was null for %c", opcode);
            return;
        }
        try {
            callbackData.method.invoke(callbackData.listener, serverHandle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch ( IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
