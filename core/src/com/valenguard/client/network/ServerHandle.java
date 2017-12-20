package com.valenguard.client.network;

import com.valenguard.client.network.shared.Write;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by unene on 12/20/2017.
 */

@AllArgsConstructor
@Getter
public class ServerHandle {
    private Client client;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public String readString() {
        try {
            return inStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte readByte() {
        try {
            return inStream.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0x0;
    }

    public Object readObject() {
        try {
            return inStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(char opcode, Write writeCallback) {
        client.send(opcode, writeCallback);
    }
}
