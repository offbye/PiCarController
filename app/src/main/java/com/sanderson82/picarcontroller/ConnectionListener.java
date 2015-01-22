package com.sanderson82.picarcontroller;

/**
 * Created by Casa on 1/14/2015.
 */
public interface ConnectionListener {
    public void clientConnection();

    public void clientDisconnected();

    public void connectionError();
}
