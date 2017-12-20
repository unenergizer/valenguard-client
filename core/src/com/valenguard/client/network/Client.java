package com.valenguard.client.network;

import java.io.IOException;
import java.net.SocketException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.valenguard.client.network.shared.Opcodes;
import com.valenguard.client.network.shared.Write;
import com.valenguard.client.util.Consumer;

import lombok.Getter;

/**
 * Created by unene on 12/20/2017.
 */

public class Client {

    private Socket socket;
    private final int SECONDS_TO_TIMEOUT = 5;

    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    private ServerHandle serverHandle;

    private @Getter
    volatile boolean running = false;

    private ClientEventBus eventBus = new ClientEventBus();

    public void connect(final String address, final short port, final Consumer<ClientEventBus> registerListeners) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    socket = new Socket();
                    socket.connect(new InetSocketAddress(address, port), 1000 * SECONDS_TO_TIMEOUT);

                } catch (IOException e) {

                    // Failed to connect
                    if (e instanceof ConnectException) {

                        return;
                    } else {
                        e.printStackTrace();
                    }
                }

                // Setup the network due to successful connection
                // ----------------------------------------------
                System.out.println("CONNECTION MADE");

                running = true;

                try {
                    outStream = new ObjectOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                registerListeners.accept(eventBus);

                receivePackets();

                // TODO: SEND LOGIN REQUEST TO SERVER
                send(Opcodes.EXAMPLE_OPCODE, new Write() {
                    @Override
                    public void accept(ObjectOutputStream outStream) throws IOException {

                    }
                });
            }
        }, "Connection").start();
    }

    private void receivePackets() {
        try {

            inStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        serverHandle = new ServerHandle(this, outStream, inStream);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {

                        eventBus.publish(inStream.readChar(), serverHandle);

                    } catch (IOException e) {
                        // Socket closed
                        if (!(e instanceof SocketException && !running)) {
                            break;
                        }
                    }
                }
            }
        }, "receive_packets").start();
    }

    public void send(final char opcode, Write writeCallback) {
        try {
            outStream.writeChar(opcode);
            writeCallback.accept(outStream);
            outStream.flush();
        } catch (IOException e) {
            if (!(e instanceof SocketException)) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        running = false;
        try {
            if (socket != null) socket.close();
            if (outStream != null) outStream.close();
            if (inStream != null) inStream.close();
            socket = null;
            outStream = null;
            inStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // Double ensuring that the network is closed
        close();
    }
}
