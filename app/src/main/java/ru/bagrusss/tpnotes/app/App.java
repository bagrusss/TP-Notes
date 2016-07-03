package ru.bagrusss.tpnotes.app;

import android.app.Application;

import ru.bagrusss.tpnotes.services.ServiceHelper;
import ru.bagrusss.tpnotes.utils.FilesStorage;

/**
 * Created by bagrusss.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FilesStorage.createRootDir();
        ServiceHelper.scanNotes(this);
    }
}
