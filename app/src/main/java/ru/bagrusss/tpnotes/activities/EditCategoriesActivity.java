package ru.bagrusss.tpnotes.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ru.bagrusss.tpnotes.R;

public class EditCategoriesActivity extends BaseActivity {

    public static final int CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
