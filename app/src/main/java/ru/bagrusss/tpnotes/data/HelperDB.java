package ru.bagrusss.tpnotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bagrusss.
 */

public class HelperDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "tp_notes.db";
    private static final int VERSION = 1;

    public HelperDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
