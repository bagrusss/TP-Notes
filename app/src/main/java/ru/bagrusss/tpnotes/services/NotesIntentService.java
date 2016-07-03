package ru.bagrusss.tpnotes.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.EditCategoriesActivity;
import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.eventbus.Message;
import ru.bagrusss.tpnotes.fragments.NotesFragment;

public class NotesIntentService extends IntentService {

    public static final String ACTION_SAVE_NOTE = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_NOTE";
    public static final String ACTION_SAVE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_CATEGORY";
    public static final String ACTION_SCAN_NOTES = "ru.bagrusss.tpnotes.services.action.ACTION_SCAN_NOTES";

    public static final String COLOR = HelperDB.COLOR;
    public static final String NAME = HelperDB.NAME;

    public static final int STATUS_SCAN_OK = 3;
    public static final String ACTION_UPDATE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_UPDATE_CATEGORY";

    public NotesIntentService() {
        super("NotesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            long res = 0;
            switch (action) {
                case ACTION_SCAN_NOTES:
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit().putBoolean(getString(R.string.key_sync), false).commit();
                    HelperDB.getInstance(this).scanNotes();
                    EventBus.getDefault()
                            .post(new Message(NotesFragment.REQUEST_CODE, STATUS_SCAN_OK));
                    break;
                case ACTION_SAVE_CATEGORY:
                    res = HelperDB.getInstance(this)
                            .insertCategory(intent.getStringExtra(NAME), intent.getStringExtra(COLOR));
                    if (res != -1)
                        EventBus.getDefault()
                                .post(new Message(EditCategoriesActivity.REQUEST_CODE, Message.OK));
                    else
                        EventBus.getDefault()
                                .post(new Message(EditCategoriesActivity.REQUEST_CODE, Message.ERROR));
                    break;

                case ACTION_UPDATE_CATEGORY:
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
                    break;

            }

        }
    }

}
