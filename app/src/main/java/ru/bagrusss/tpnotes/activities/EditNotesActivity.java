package ru.bagrusss.tpnotes.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;
import ru.bagrusss.tpnotes.fragments.BaseListFragment;

public final class EditNotesActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mLockState = false;
    private MenuItem mLockItem;
    private EditText mNoteText;

    private Spinner mCategory;
    private CategoryAdapter mCategoryAdapter;

    public static final int CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mNoteText = (EditText) findViewById(R.id.note_text);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent internalIntent = getIntent();
        mLockState = internalIntent.getBooleanExtra("lock", false);

        mCategory = (Spinner) findViewById(R.id.spinner_category);
        mCategoryAdapter = new CategoryAdapter(this, null, R.layout.item_category);
        if (mCategory != null) {
            mCategory.setAdapter(mCategoryAdapter);
        }
        getLoaderManager().initLoader(BaseListFragment.CategoriesLoader.ID, null, this);
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

                break;
            case R.id.save_action:
                save();
                break;
            default:
                res = false;
        }
        return res || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    private void setLockIcon(boolean lock) {
        mLockItem.setIcon(lock ?
                R.drawable.ic_locked_white :
                R.drawable.ic_locked_no_white);
    }

    @Override
    protected void save() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new BaseListFragment.CategoriesLoader(this, false);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        mCategoryAdapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
