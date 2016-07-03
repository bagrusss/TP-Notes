package ru.bagrusss.tpnotes.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bagrusss.
 */
public class DateTime {

    public static String get() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.ROOT);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
