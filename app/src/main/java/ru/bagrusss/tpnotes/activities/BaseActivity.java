package ru.bagrusss.tpnotes.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.bagrusss.tpnotes.R;

/**
 * Created by bagrusss.
 */

public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected int mThemeId;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String theme = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.key_theme), "theme1");
        if (theme.equals("theme2"))
            setTheme(R.style.Theme_Dark_NoActionBar);
        super.onCreate(savedInstanceState);
    }
}
