package ru.bagrusss.tpnotes.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.EditCategoriesActivity;
import ru.bagrusss.tpnotes.activities.EditNotesActivity;
import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.eventbus.Message;
import ru.bagrusss.tpnotes.fragments.CategoriesFragment;
import ru.bagrusss.tpnotes.fragments.NotesFragment;
import ru.bagrusss.tpnotes.utils.FilesStorage;

public class NotesIntentService extends IntentService {

    public static final String ACTION_SAVE_NOTE = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_NOTE";
    public static final String ACTION_UPDATE_NOTE = "ru.bagrusss.tpnotes.services.action.ACTION_UPDATE_NOTE";
    public static final String ACTION_DELETE_NOTE = "ru.bagrusss.tpnotes.services.action.ACTION_DELETE_NOTE";
    public static final String ACTION_SCAN_NOTES = "ru.bagrusss.tpnotes.services.action.ACTION_SCAN_NOTES";

    public static final String ACTION_SAVE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_CATEGORY";
    public static final String ACTION_UPDATE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_UPDATE_CATEGORY";
    public static final String ACTION_DELETE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_DELETE_CATEGORY";


    public static final String COLOR = HelperDB.COLOR;
    public static final String NAME = HelperDB.NAME;

    public static final int STATUS_SCAN_OK = 3;

    public NotesIntentService() {
        super("NotesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_SCAN_NOTES:
                    scanNotes();
                    break;
                case ACTION_SAVE_CATEGORY:
                    saveCategory(intent);
                    break;
                case ACTION_UPDATE_CATEGORY:
                    updateCategory(intent);
                    break;
                case ACTION_DELETE_CATEGORY:
                    deleteCategory(intent);
                    break;

                case ACTION_SAVE_NOTE:
                    saveNote(intent);
                    break;
                case ACTION_UPDATE_NOTE:

                    break;
                case ACTION_DELETE_NOTE:

                    break;

            }

        }
    }

    private void saveNote(Intent intent) {
        String category = intent.getStringExtra(HelperDB.CATEGORY);
        String color = intent.getStringExtra(HelperDB.COLOR);
        String text = intent.getStringExtra(HelperDB.FIRST_STRING);
        String filename = intent.getStringExtra(FilesStorage.FILE_NAME);
        try {
            FilesStorage.writeNote(filename, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int last = text.indexOf("\n");
        String first = text.substring(0, last >= 0 ? last : text.length() - 1);
        HelperDB.getInstance(this).insertNote(filename, first, category, color);
        EventBus.getDefault().post(new Message(EditNotesActivity.REQUEST_CODE, Message.OK));
    }

    private void deleteCategory(Intent intent) {
        long id = intent.getLongExtra(HelperDB.ID, 0);
        HelperDB.getInstance(this).deleteCategory(id);
        if (id > 0)
            EventBus.getDefault()
                    .post(new Message(CategoriesFragment.REQUEST_CODE, Message.OK));
    }

    private void scanNotes() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putBoolean(getString(R.string.key_sync), false).commit();
        HelperDB.getInstance(this).scanNotes();
        EventBus.getDefault()
                .post(new Message(NotesFragment.REQUEST_CODE, STATUS_SCAN_OK));
    }

    private void saveCategory(Intent intent) {
        long res = HelperDB.getInstance(this)
                .insertCategory(intent.getStringExtra(NAME), intent.getStringExtra(COLOR));
        if (res != -1)
            EventBus.getDefault()
                    .post(new Message(EditCategoriesActivity.REQUEST_CODE, Message.OK));
        else
            EventBus.getDefault()
                    .post(new Message(EditCategoriesActivity.REQUEST_CODE, Message.ERROR));
    }

    private void updateCategory(Intent intent) {
        long res;
        try {
            res = HelperDB.getInstance(this)
                    .updateCategory(intent.getLongExtra(HelperDB.ID, 0),
                            intent.getStringExtra(HelperDB.NAME),
                            intent.getStringExtra(HelperDB.COLOR));
        } catch (SQLiteException e) {
            EventBus.getDefault()
                    .post(new Message(EditCategoriesActivity.REQUEST_CODE, Message.ERROR));
            return;
        }
        if (res != -1)
            EventBus.getDefault()
                    .post(new Message(EditCategoriesActivity.REQUEST_CODE, Message.OK));
    }

}
