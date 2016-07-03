package ru.bagrusss.tpnotes.services;

import android.content.Context;
import android.content.Intent;

/**
 * Created by bagrusss.
 */

public class ServiceHelper {

    public static void saveNote(Context context, String text, String category) {

    }

    public static void readNote(Context context, String filename) {

    }

    public static void deleteNote(Context context, String filename) {

    }

    public static void saveCategory(Context context, String category, String hexColor) {

    }

    public static void readCategory(Context context, String category) {

    }

    public static void deleteCategory(Context context, String category) {

    }

    public static void scanNotes(Context context) {
        Intent intent = new Intent(context, FileIntentService.class);
        intent.setAction(FileIntentService.ACTION_SCAN_NOTES);
        context.startService(intent);
    }
}
