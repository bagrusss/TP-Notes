package ru.bagrusss.tpnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ru.bagrusss.tpnotes.R;

public class EditActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private boolean mLockState = false;
    private MenuItem mLockItem;
    private EditText mNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNoteText = (EditText) findViewById(R.id.note_text);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent internalIntent = getIntent();
        mLockState = internalIntent.getBooleanExtra("lock", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        mLockItem = menu.findItem(R.id.lock_action);
        setLockIcon(mLockState);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = true;
        switch (item.getItemId()) {
            case R.id.lock_action:
                mLockState = !mLockState;
                setLockIcon(mLockState);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.share_action:

                break;
            default:
                res = false;
        }
        return res;
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    private void setLockIcon(boolean lock) {
        mLockItem.setIcon(lock ?
                R.drawable.ic_lock_outline_white_24dp :
                R.drawable.ic_lock_open_white_24dp);
    }

    private void save() {

    }
}
