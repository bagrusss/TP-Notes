package ru.bagrusss.tpnotes.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.EditNotesActivity;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;
import ru.bagrusss.tpnotes.adapters.NotesAdapter;
import ru.bagrusss.tpnotes.data.HelperDB;
import ru.bagrusss.tpnotes.eventbus.Message;
import ru.bagrusss.tpnotes.services.NotesIntentService;
import ru.bagrusss.tpnotes.services.ServiceHelper;

/**
 * Created by bagrusss.
 */

public class NotesFragment extends BaseListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private View mSpinnerContainer;
    private NotesAdapter mNotesAdapter;
    private int loadersCount = 2;
    public static final int REQUEST_CODE = 30;

    @Override
    int getReqCode() {
        return REQUEST_CODE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, root, savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mSpinnerContainer = LayoutInflater.from(getActivity())
                .inflate(R.layout.toolbar_spinner, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCategoryAdapter = new CategoryAdapter(getActivity(), null, R.layout.item_spinner);
        Spinner categoriesSpinner = (Spinner) mSpinnerContainer.findViewById(R.id.category_spinner);
        categoriesSpinner.setAdapter(mCategoryAdapter);
        categoriesSpinner.setOnItemSelectedListener(this);
        toolbar.addView(mSpinnerContainer, lp);
        mNotesAdapter = new NotesAdapter(getActivity(), null);
        mListView.setAdapter(mNotesAdapter);
        mListView.setOnItemClickListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.getBoolean(getString(R.string.key_sync), false))
            initLoaders();
        EventBus.getDefault().register(this);
        return v;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.removeView(mSpinnerContainer);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Message msg) {
        if (msg.reqCode == REQUEST_CODE) {
            if (msg.status == NotesIntentService.STATUS_SCAN_OK) {
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit().putBoolean(getString(R.string.key_sync), true).commit();
                initLoaders();
            } else getLoaderManager().getLoader(NotesLoader.ID).forceLoad();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editNote(view, position);
    }

    private void editNote(View view, long id) {
        NotesAdapter.ViewHolder holder = (NotesAdapter.ViewHolder) view.getTag();
        Intent intent = new Intent(getActivity(), EditNotesActivity.class);
        intent.setAction(EditNotesActivity.ACTION_EDIT);
        intent.putExtra(EditNotesActivity.KEY_LOCKED, false);
        intent.putExtra(EditNotesActivity.FILE_NAME, holder.name);
        intent.putExtra(EditNotesActivity.KEY_CATEGORY, holder.category.getText().toString());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                NotesAdapter.ViewHolder holder = (NotesAdapter.ViewHolder) info.targetView.getTag();
                String filename = holder.name;
                ServiceHelper.deleteNote(getActivity(), filename);
                break;
            case 0:
                editNote(info.targetView, info.id);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reloadNotes();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reloadNotes() {
        getLoaderManager().getLoader(NotesLoader.ID).forceLoad();
    }

    public static NotesFragment newInstance(int activityCode) {
        Bundle args = new Bundle();
        args.putInt(ACTIVITY_CODE, activityCode);
        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initLoaders() {
        loadCategories(this);
        Bundle args = new Bundle();
        args.putString(NotesLoader.SEARCH_CATEGORY, "");
        getLoaderManager().initLoader(NotesLoader.ID, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        switch (id) {
            case CategoriesLoader.ID:
                loader = new CategoriesLoader(getActivity(), true);
                break;
            case NotesLoader.ID:
                String cat = args.getString(NotesLoader.SEARCH_CATEGORY);
                loader = new NotesLoader(getActivity(), cat);
                break;
            default:
                return null;

        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case CategoriesLoader.ID:
                mCategoryAdapter.swapCursor(c);
                --loadersCount;
                break;
            case NotesLoader.ID:
                mNotesAdapter.swapCursor(c);
                --loadersCount;
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CategoryAdapter.ViewHolder holder = (CategoryAdapter.ViewHolder) view.getTag();
        String category = position == 0 ? "" : holder.text.getText().toString();
        Bundle args = new Bundle();
        args.putString(NotesLoader.SEARCH_CATEGORY, category);
        getLoaderManager().restartLoader(NotesLoader.ID, args, this);
        ++loadersCount;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static class NotesLoader extends CursorLoader {

        public static final int ID = 200;
        public static final String SEARCH_CATEGORY = "search";
        private String mCategory;

        public NotesLoader(Context context, String category) {
            super(context);
            mCategory = category;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor c;
            if ("".equals(mCategory))
                c = HelperDB.getInstance(getContext()).allNotes();
            else c = HelperDB.getInstance(getContext()).notesWithCategory(mCategory);
            return c;
        }
    }
}
