package ru.bagrusss.tpnotes.eventbus;

/**
 * Created by bagrusss.
 */
public class Message {

    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final int ACTION_SAVED = 2;
    public static final int ACTION_UPDATED = 2;

    public int reqCode;
    public int status;
    public int action;
    public long id;

    public Message(int reqCode, int status) {
        this.reqCode = reqCode;
        this.status = status;
    }
}
