package ru.bagrusss.tpnotes.services;

import android.content.Context;
import android.content.Intent;

import ru.bagrusss.tpnotes.activities.EditNotesActivity;
import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.utils.FilesStorage;

/**
 * Created by bagrusss.
 */

public class ServiceHelper {


    private static Intent putNote(Context context, String filename, String text, String category, String color) {
        Intent intent = new Intent(context, NotesIntentService.class);
        intent.putExtra(HelperDB.CATEGORY, category);
        intent.putExtra(FilesStorage.FILE_NAME, filename);
        intent.putExtra(HelperDB.FIRST_STRING, text);
        intent.putExtra(HelperDB.COLOR, color);
        return intent;
    }

    public static void saveNote(Context context, String filename, String text, String category, String color) {
        Intent intent = putNote(context, filename, text, category, color);
        intent.setAction(NotesIntentService.ACTION_SAVE_NOTE);
        context.startService(intent);
    }

    public static void updateNote(Context context, String filename, String text, String category, String color) {
        Intent intent = putNote(context, filename, text, category, color);
        intent.setAction(NotesIntentService.ACTION_UPDATE_NOTE);
        context.startService(intent);
    }

    public static void readNote(Context context, String filename) {
        Intent intent = new Intent(context, NotesIntentService.class);
        intent.setAction(NotesIntentService.ACTION_READ_NOTE);
        intent.putExtra(EditNotesActivity.FILE_NAME, filename);
        context.startService(intent);
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

    public static void deleteCategory(Context context, long id) {
        Intent intent = new Intent(context, NotesIntentService.class);
        intent.setAction(NotesIntentService.ACTION_DELETE_CATEGORY);
        intent.putExtra(HelperDB.ID, id);
        context.startService(intent);
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
