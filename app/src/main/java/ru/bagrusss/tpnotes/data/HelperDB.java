package ru.bagrusss.tpnotes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ru.bagrusss.tpnotes.utils.FilesStorage;

/**
 * Created by bagrusss.
 */

public class HelperDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "tp_notes.db";
    private static final int VERSION = 1;

    public static final String TABLE_NOTES = "Note";
    public static final String TABLE_CATEGORIES = "Categories";

    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String FIRST_STRING = "first_string";
    public static final String CATEGORY = "category";
    public static final String COLOR = "color";

    private static final String CREATE_NOTES = "CREATE TABLE " + TABLE_NOTES + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT UNIQUE, " +
            FIRST_STRING + " TEXT, " +
            COLOR + " TEXT, " +
            CATEGORY + " TEXT);";

    private static final String CREATE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT UNIQUE, " +
            COLOR + " TEXT);"; //hex val

    private static final String DELETE_NOTE = "DELETE FROM " + TABLE_NOTES +
            " WHERE " + NAME + "=?;";

    private static HelperDB mInstance;
    private static SQLiteDatabase mDB;

    private HelperDB(Context context) {
        super(context, DB_NAME, null, VERSION);
        mDB = getWritableDatabase();
    }

    public static HelperDB getInstance(Context context) {
        HelperDB localInstance = mInstance;
        if (localInstance == null) {
            synchronized (HelperDB.class) {
                localInstance = mInstance;
                if (localInstance == null) {
                    mInstance = localInstance = new HelperDB(context);
                }
            }
        }
        return localInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTES);
        db.execSQL(CREATE_CATEGORIES);
        ContentValues cv = new ContentValues();
        cv.put(NAME, "");
        cv.put(COLOR, "#00000000");
        db.insert(TABLE_CATEGORIES, null, cv);
        cv.put(NAME, "temp");
        cv.put(COLOR, "green");

        db.insert(TABLE_CATEGORIES, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void scanNotes() {
        SQLiteStatement deleteStatement = mDB.compileStatement(DELETE_NOTE);
        Cursor c = mDB.query(TABLE_NOTES, new String[]{NAME}, null, null, null, null, null);
        List<String> forDelete = new LinkedList<>();
        while (c.moveToNext()) {
            String filename = c.getString(c.getColumnIndex(NAME));
            File f = FilesStorage.getNote(filename);
            if (f == null)
                forDelete.add(filename);
        }
        c.close();
        for (String fl : forDelete) {
            deleteStatement.bindString(1, fl);
            deleteStatement.executeUpdateDelete();
        }
        deleteStatement.close();
    }

    public void insertNote(String name, String first, String category, String color) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(FIRST_STRING, first);
        cv.put(CATEGORY, category);
        cv.put(COLOR, color);
        mDB.insert(TABLE_NOTES, null, cv);
    }

    public long insertCategory(String name, String color) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(COLOR, color);
        return mDB.insert(TABLE_CATEGORIES, null, cv);
    }

    public long updateCategory(long id, String name, String color) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(COLOR, color);
        String oldName;
        Cursor c = mDB.query(TABLE_CATEGORIES, new String[]{NAME}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        c.moveToFirst();
        oldName = c.getString(c.getColumnIndex(NAME));
        c.close();
        long res = mDB.update(TABLE_CATEGORIES, cv, ID + "=?", new String[]{String.valueOf(id)});
        cv.clear();
        cv.put(CATEGORY, name);
        res += mDB.update(TABLE_NOTES, cv, CATEGORY + "=?", new String[]{oldName});
        return res;
    }

    public long deleteCategory(String cat) {
        mDB.beginTransaction();
        long res;
        try {
            res = mDB.delete(TABLE_CATEGORIES, NAME + "=?", new String[]{cat});
            res += mDB.delete(TABLE_NOTES, CATEGORY + "=?", new String[]{cat});
            mDB.setTransactionSuccessful();
        } finally {
            mDB.endTransaction();
        }
        return res;
    }

    public Cursor allNotes() {
        return mDB.query(TABLE_NOTES, null, null, null, null, null, null);
    }

    public Cursor allCategories() {
        return mDB.query(TABLE_CATEGORIES, null, null, null, null, null, NAME);
    }

    public Cursor notAllCategories() {
        return mDB.query(TABLE_CATEGORIES, null, ID + "!=1", null, null, null, NAME);
    }

    public Cursor notesWithCategory(String category) {
        return mDB.query(TABLE_NOTES, null, CATEGORY + "=?", new String[]{category}, null, null, NAME);
    }

    public static void closeDB() {
        if (mDB != null && mDB.isOpen())
            mDB.close();
        mInstance = null;
    }

    public void updateNote(String filename, String first, String category, String color) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, filename);
        cv.put(FIRST_STRING, first);
        cv.put(CATEGORY, category);
        cv.put(COLOR, color);
        mDB.update(TABLE_NOTES, cv, NAME + "=?", new String[]{filename});
    }

    public long deleteNote(String file) {
        return mDB.delete(TABLE_NOTES, NAME + "=?", new String[]{file});

    }
}
