package ru.bagrusss.tpnotes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

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
            CATEGORY + " TEXT);";

    private static final String CREATE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT UNIQUE, " +
            COLOR + " TEXT);"; //hex val

    private static final String INSERT_CATEGORY = "INSERT OR IGNORE INTO " + TABLE_CATEGORIES +
            " (" + NAME + ',' + COLOR + ')' + " VALUES(?,?);";

    private static final String INSERT_NOTE = "INSERT OR IGNORE INTO " + TABLE_NOTES +
            " ( " + NAME + ',' + FIRST_STRING + ',' + CATEGORY + ')' + " VALUES(?,?,?);";

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
        cv.put(NAME, "default");
        cv.put(COLOR, "#FFFF33");
        db.insert(TABLE_CATEGORIES, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void scanNotes() {
        File file = FilesStorage.getNotesDir();
        File files[] = file.listFiles();
        mDB.delete(TABLE_NOTES, null, null);
        mDB.delete(TABLE_CATEGORIES, null, null);
        SQLiteStatement statementNote = mDB.compileStatement(INSERT_NOTE);
        SQLiteStatement statementCategory = mDB.compileStatement(INSERT_CATEGORY);
        for (File f : files) {
            String fName = f.getName();
            String category = fName.substring(fName.indexOf("__") + 2, fName.indexOf("."));
            String name = fName.substring(0, fName.indexOf("__"));
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
            statementNote.executeInsert();
            statementCategory.bindString(1, category);
            statementCategory.bindString(2, "#000000");
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

    public Cursor allNotes() {
        return mDB.query(TABLE_NOTES, null, null, null, null, null, null);
    }

    public Cursor allCategories() {
        return mDB.query(TABLE_CATEGORIES, null, null, null, null, null, null);
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
