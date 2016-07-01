package ru.bagrusss.tpnotes.services;

import android.app.IntentService;
import android.content.Intent;


public class FileIntentService extends IntentService {

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "ru.bagrusss.tpnotes.services.extra.PARAM1";

    public FileIntentService() {
        super(FileIntentService.class.getClass().getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
        }
    }


}
