package ru.bagrusss.tpnotes.app;

import android.app.Application;

import java.io.IOException;

import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.utils.FilesStorage;

/**
 * Created by bagrusss.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FilesStorage.createRootDir();
        try {
            FilesStorage.createNote("1__10.txt");
            FilesStorage.createNote("2__10.txt");
            FilesStorage.createNote("3__11.txt");
            FilesStorage.createNote("4__11.txt");
            FilesStorage.createNote("5__12.txt");
            FilesStorage.createNote("6__12.txt");
            FilesStorage.createNote("7__12.txt");
            HelperDB.getInstance(this).scanNotes();
            HelperDB.closeDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
