package ru.bagrusss.tpnotes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ru.bagrusss.tpnotes.utils.FilesStorage;

/**
 * Created by bagrusss.
 */

public class HelperDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "tp_notes.db";
    private static final int VERSION = 1;

    public static final String TABLE_NOTES = "Notes";
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

    private static final String INSERT_CATEGORY = "INSERT OR IGNORE INTO " + TABLE_CATEGORIES +
            " (" + NAME + ',' + COLOR + ')' + " VALUES(?,?);";

    private static final String INSERT_NOTE = "INSERT OR IGNORE INTO " + TABLE_NOTES +
            " ( " + NAME + ',' + FIRST_STRING + ',' + CATEGORY + ',' + COLOR + ')'
            + " VALUES(?,?,?,?);";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void scanNotes() {
        File file = FilesStorage.getNotesDir();
        File files[] = file.listFiles();
        mDB.delete(TABLE_NOTES, null, null);
        SQLiteStatement statementNote = mDB.compileStatement(INSERT_NOTE);
        SQLiteStatement statementCategory = mDB.compileStatement(INSERT_CATEGORY);
        for (File f : files) {
            String fName = f.getName();
            int categorySeparator = fName.indexOf("__");
            int colorSeparator = fName.indexOf("-");
            int fileExtSeparator = fName.indexOf(".");
            int res = categorySeparator | colorSeparator | fileExtSeparator;
            if (res < 0)
                return;
            String color = fName.substring(colorSeparator + 1, fileExtSeparator);
            try {
                Color.parseColor(color);
            } catch (IllegalArgumentException e) {
                return;
            }
            String category = fName.substring(categorySeparator + 2, colorSeparator);
            String name = fName.substring(0, categorySeparator);
            BufferedReader reader = null;
            String first = null;
            try {
                reader = new BufferedReader(new FileReader(f));
                first = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            statementNote.bindString(1, name);
            statementNote.bindString(2, first == null ? "" : first);
            statementNote.bindString(3, category);
            statementNote.bindString(4, color);
            statementNote.executeInsert();
            statementCategory.bindString(1, category);
            statementCategory.bindString(2, color);
            statementCategory.executeInsert();
        }
        statementNote.close();
        statementCategory.close();
    }

    public void insertNote(String name, String first, String category) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(FIRST_STRING, first);
        cv.put(CATEGORY, category);
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

    public Cursor allNotes() {
        return mDB.query(TABLE_NOTES, null, null, null, null, null, null);
    }

    public Cursor allCategories() {
        return mDB.query(TABLE_CATEGORIES, null, null, null, null, null, null);
    }

    public Cursor notAllCategories() {
        return mDB.query(TABLE_CATEGORIES, null, ID + "!=1", null, null, null, null);
    }

    public Cursor notesWithCategory(String category) {
        return mDB.query(TABLE_NOTES, null, CATEGORY + "=?", new String[]{category}, null, null, NAME);
    }

    public static void closeDB() {
        if (mDB != null && mDB.isOpen())
            mDB.close();
        mInstance = null;
    }
}
