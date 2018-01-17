package com.valenguard.client.network;

import com.valenguard.client.network.shared.Write;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
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

@AllArgsConstructor
@Getter
public class ServerHandle {

    private Client client;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public String readString() {
        try {
            return inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte readByte() {
        try {
            return inputStream.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0x00;
    }

    public Object readObject() {
        try {
            return inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(final char opcode, Write writeCallback) {
        try {
            outputStream.writeChar(opcode);
            writeCallback.accept(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            if (!(e instanceof SocketException)) {
                e.printStackTrace();
            }
        }
    }

    void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            socket = null;
            outputStream = null;
            inputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
