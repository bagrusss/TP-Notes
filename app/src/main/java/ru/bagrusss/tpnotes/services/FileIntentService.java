package ru.bagrusss.tpnotes.services;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.eventbus.Message;

public class FileIntentService extends IntentService {

    public static final String ACTION_SAVE_NOTE = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_NOTE";
    public static final String ACTION_SAVE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_CATEGORY";
    public static final String ACTION_SCAN_NOTES = "ru.bagrusss.tpnotes.services.action.ACTION_SCAN_NOTES";

    public static final String EXTRA_PARAM1 = "ru.bagrusss.tpnotes.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "ru.bagrusss.tpnotes.services.extra.PARAM2";

    public FileIntentService() {
        super("FileIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_SCAN_NOTES:
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit().putBoolean(getString(R.string.key_sync), false).commit();
                    HelperDB.getInstance(this).scanNotes();
                    EventBus.getDefault().post(new Message());
                    break;
                case ACTION_SAVE_CATEGORY:

                    break;
                case ACTION_SAVE_NOTE:

                    break;

            }

        }
    }

}
