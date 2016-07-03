package ru.bagrusss.tpnotes.services;

import android.content.Context;
import android.content.Intent;

import ru.bagrusss.tpnotes.data.HelperDB;

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

    public static void saveCategory(Context context, String name, String hexColor) {
        Intent intent = new Intent(context, NotesIntentService.class);
        intent.setAction(NotesIntentService.ACTION_SAVE_CATEGORY);
        intent.putExtra(NotesIntentService.COLOR, hexColor);
        intent.putExtra(NotesIntentService.NAME, name);
        context.startService(intent);
    }

    public static void readCategory(Context context, String category) {

    }

    public static void deleteCategory(Context context, String category) {

    }

    public static void scanNotes(Context context) {
        Intent intent = new Intent(context, NotesIntentService.class);
        intent.setAction(NotesIntentService.ACTION_SCAN_NOTES);
        context.startService(intent);
    }

    public static void updateCategory(Context context, long id, String category, String color) {
        Intent intent = new Intent(context, NotesIntentService.class);
        intent.setAction(NotesIntentService.ACTION_UPDATE_CATEGORY);
        intent.putExtra(HelperDB.ID, id);
        intent.putExtra(HelperDB.NAME, category);
        intent.putExtra(HelperDB.COLOR, color);
        context.startService(intent);
    }
}
