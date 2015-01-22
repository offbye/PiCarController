package com.sanderson82.picarcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

/**
 * A simple socket connection to send commands to the pi
 */
public enum ClientController {
    INSTANCE;

    private Socket outputSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Vector<ConnectionListener> listeners = new Vector<ConnectionListener>();

    /**
     * Connects the client to the server
     * @param server
     * @param port
     */
    public void connect(final String server, final int port)
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName(server);
                    outputSocket = new Socket(server, port);

                    System.out.println("Connected on port:" + port);

                    out = new PrintWriter(outputSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(outputSocket.getInputStream()));
                    notifyOnConnect();
                } catch (IOException ex) {
                    System.err.println("Couldn't get IO for the connection to the server " + ex.toString());
                    notifyOnConnectError();
                }
            }
        });
        t.start();

    }

    /**
     * Sends a command to the server
     *
     * @param command The command
     * @param value The value of the command
     */
    public void sendCommand(int command, int value) {
        System.out.println("Sending command:" + (int) command + " value = " + (int) value);
        out.println(command + " " + value);
        out.flush();
    }

    /**
     * Disconnect the client from the server
     */
    public void disconnect()
    {
        if(isConnected())
        {
            //TODO:  Fill in the client disconnect procedure
        }
    }

    /**
     * Checks to see if the client is connected to the server
     * @return
     */
    public boolean isConnected()
    {
        return outputSocket==null?false:outputSocket.isConnected();
    }

    public void addConnectionListener(ConnectionListener cl) {
        listeners.add(cl);
    }

    public void removeConnectionListener(ConnectionListener cl) {
        listeners.remove(cl);
    }

    private void notifyOnConnect() {
        for (ConnectionListener cl : listeners)
            cl.clientConnection();
    }

    private void notifyOnDisconnect() {
        for (ConnectionListener cl : listeners)
            cl.clientDisconnected();
    }

    private void notifyOnConnectError() {
        for (ConnectionListener cl : listeners)
            cl.connectionError();
    }


}
