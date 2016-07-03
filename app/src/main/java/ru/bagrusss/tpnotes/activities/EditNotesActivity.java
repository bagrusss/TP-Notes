package ru.bagrusss.tpnotes.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;
import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.eventbus.Message;
import ru.bagrusss.tpnotes.eventbus.TextMessage;
import ru.bagrusss.tpnotes.fragments.BaseListFragment;
import ru.bagrusss.tpnotes.services.ServiceHelper;
import ru.bagrusss.tpnotes.utils.DateTime;
import ru.bagrusss.tpnotes.utils.FilesStorage;

public final class EditNotesActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mLockState = false;
    private MenuItem mLockItem;
    private EditText mNoteText;

    private Spinner mCategorySpinner;
    private CategoryAdapter mCategoryAdapter;
    private String mCategory;
    private String mColor;

    public static final int REQUEST_CODE = 10;
    public static final String KEY_LOCKED = "LOCKED";
    public static final String FILE_NAME = "FILE_NAME";

    private boolean result = false;
    private boolean back = false;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mNoteText = (EditText) findViewById(R.id.note_text);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNoteText = (EditText) findViewById(R.id.note_text);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        mCategorySpinner = (Spinner) findViewById(R.id.spinner_category);
        mCategoryAdapter = new CategoryAdapter(this, null, R.layout.item_category);
        if (mCategorySpinner != null) {
            mCategorySpinner.setAdapter(mCategoryAdapter);
            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CategoryAdapter.ViewHolder holder = (CategoryAdapter.ViewHolder) view.getTag();
                    mCategory = holder.text.getText().toString();
                    mColor = holder.colorVal;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        getLoaderManager().initLoader(BaseListFragment.CategoriesLoader.ID, null, this);
        mAction = intent.getAction();
        if (mAction.equals(ACTION_NEW)) {
            mFileName = FilesStorage.PREFIX + DateTime.get() + FilesStorage.EXT;
        } else {
            mCategory = intent.getStringExtra(KEY_CATEGORY);
            mFileName = intent.getStringExtra(FILE_NAME);
            loadNote(mFileName);
        }
        EventBus.getDefault().register(this);

    }

    private void setSpinner() {
        for (int i = 0; i < mCategorySpinner.getCount(); i++) {
            Cursor c = (Cursor) mCategorySpinner.getItemAtPosition(i);
            String cat = c.getString(c.getColumnIndex(HelperDB.NAME));
            if (cat.equals(mCategory)) {
                mCategorySpinner.setSelection(i);
                break;
            }
        }
    }

    private void loadNote(String mFileName) {
        ServiceHelper.readNote(this, mFileName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
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
            case R.id.share_action:
                save();
                share();
                break;
            case R.id.save_action:
                save();
                break;
            default:
                res = false;
        }
        return res || super.onOptionsItemSelected(item);
    }

    private void share() {

    }

    @Override
    public void onBackPressed() {
        if (mNoteText.getText().length() == 0)
            super.onBackPressed();
        if (result) {
            setResult(RESULT_OK);
            super.onBackPressed();
            return;
        }
        save();
        back = true;
    }

    private void setLockIcon(boolean lock) {
        mLockItem.setIcon(lock ?
                R.drawable.ic_locked_white :
                R.drawable.ic_locked_no_white);
    }

    @Override
    protected void save() {
        String text = mNoteText.getText().toString();
        if (mAction.equals(ACTION_NEW))
            ServiceHelper.saveNote(this, mFileName, text, mCategory, mColor);
        else ServiceHelper.updateNote(this, mFileName, text, mCategory, mColor);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Message msg) {
        if (msg.reqCode == REQUEST_CODE) {
            if (msg.status == Message.OK) {
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                result = true;
                if (back) {
                    setResult(RESULT_OK);
                    super.onBackPressed();
                }
            } else Toast.makeText(this, R.string.error_save, Toast.LENGTH_SHORT).show();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TextMessage msg) {
        if (msg.reqCode == REQUEST_CODE) {
            mNoteText.append(msg.line);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new BaseListFragment.CategoriesLoader(this, false);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        mCategoryAdapter.swapCursor(c);
        setSpinner();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
