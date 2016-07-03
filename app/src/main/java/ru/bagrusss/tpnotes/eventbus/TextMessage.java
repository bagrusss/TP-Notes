package ru.bagrusss.tpnotes.eventbus;

/**
 * Created by bagrusss.
 */
public class TextMessage {

    public String line;
    public int reqCode;

    public TextMessage(int reqCode, String line) {
        this.reqCode = reqCode;
        this.line = line;
    }
}
