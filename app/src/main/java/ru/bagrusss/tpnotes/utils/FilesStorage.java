package ru.bagrusss.tpnotes.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by bagrusss.
 */
public class FilesStorage {

    public static final String NOTES_DIR = "TP Notes";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static boolean createNote(String fileName) throws IOException {
        File file = new File(getNotesDir(), fileName);
        return !file.exists() && file.createNewFile();
    }

    @Nullable
    public static File getNote(@NonNull String fileName) {
        File file = new File(getNotesDir(), fileName);
        return file.exists() ? file : null;
    }

    public static boolean deleteNote(@NonNull String fileName) {
        File file = getNote(fileName);
        return file != null && file.delete();
    }

    public static File getNotesDir() {
        return new File(Environment.getExternalStorageDirectory(), NOTES_DIR);
    }

    public static boolean createRootDir() {
        if (isExternalStorageWritable() && isExternalStorageReadable()) {
            File file = getNotesDir();
            if (!file.exists())
                return file.mkdir();
        }
        Log.i(FilesStorage.class.getCanonicalName(), "Cant write to storage");
        return false;
    }
}
