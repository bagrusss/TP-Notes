package ru.bagrusss.tpnotes.services;

import android.app.IntentService;
import android.content.Intent;

public class FileIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SAVE_NOTE = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_NOTE";
    public static final String ACTION_SAVE_CATEGORY = "ru.bagrusss.tpnotes.services.action.ACTION_SAVE_CATEGORY";

    // TODO: Rename parameters
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

            }

        }
    }

}
