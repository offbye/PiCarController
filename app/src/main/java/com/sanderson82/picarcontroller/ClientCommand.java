package com.sanderson82.picarcontroller;

/**
 * Created by Casa on 12/2/2014.
 */
public enum ClientCommand {
    MOVE_FORWARD(1), MOVE_REVERSE(2), STEER_LEFT(3), STEER_RIGHT(4);
    private char value;

    private ClientCommand(int value) {
        this.value = (char) value;
    }

    public char getValue() {
        return value;
    }

}
