package ru.bagrusss.tpnotes.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;
import ru.bagrusss.tpnotes.adapters.NotesAdapter;
import ru.bagrusss.tpnotes.data.HelperDB;

/**
 * Created by bagrusss.
 */

public class NotesFragment extends BaseListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private Spinner mCategoriesSpinner;
    private View mSpinnerContainer;
    private NotesAdapter mNotesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mSpinnerContainer = LayoutInflater.from(getActivity())
                .inflate(R.layout.toolbar_spinner, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCategoryAdapter = new CategoryAdapter(getActivity(), null, R.layout.item_spinner);
        mCategoriesSpinner = (Spinner) mSpinnerContainer.findViewById(R.id.category_spinner);
        mCategoriesSpinner.setAdapter(mCategoryAdapter);
        mCategoriesSpinner.setOnItemSelectedListener(this);
        toolbar.addView(mSpinnerContainer, lp);
        mNotesAdapter = new NotesAdapter(getActivity(), null);
        mListView.setAdapter(mNotesAdapter);
        mListView.setOnItemClickListener(this);
        initLoaders();
        return v;
    }

    @Override
    public void onDestroy() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.removeView(mSpinnerContainer);
        super.onDestroy();
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
        args.putString(NotesLoader.SEARCH_CATEGORY, "<>");
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
                break;
            case NotesLoader.ID:
                mNotesAdapter.swapCursor(c);
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
