package com.sanderson82.picarcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A simple socket connection to send commands to the pi
 */
public enum ClientController {
    INSTANCE;

    Socket outputSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    /**
     * Connects the client to the server
     * @param server
     * @param port
     */
    public void connect(final String server, final int port)
    {
        Thread t = new Thread((Runnable) () -> {
            try{
                InetAddress serverAddress = InetAddress.getByName(server);
                outputSocket = new Socket(server,port);

                System.out.println("Connected on port:"+port);

                out = new PrintWriter(outputSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(outputSocket.getInputStream()));
            }
            catch(IOException ex)
            {
                System.err.println("Couldn't get IO for the connection to the server " + ex.toString());
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
    public void sendCommand(char command, char value) {
        out.print(command);
        out.print(value);
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

}
