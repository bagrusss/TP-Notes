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
import android.widget.RelativeLayout;
import android.widget.Spinner;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;

/**
 * Created by bagrusss.
 */

public class NotesFragment extends BaseListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    private Spinner mCategoriesSpinner;
    private View spinnerContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        RelativeLayout layout = (RelativeLayout) v;
        spinnerContainer = LayoutInflater.from(getActivity())
                .inflate(R.layout.toolbar_spinner, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCategoryAdapter = new CategoryAdapter(getActivity(), null, R.layout.item_spinner);
        mCategoriesSpinner = (Spinner) spinnerContainer.findViewById(R.id.category_spinner);
        mCategoriesSpinner.setAdapter(mCategoryAdapter);
        mCategoriesSpinner.setOnItemSelectedListener(this);
        toolbar.addView(spinnerContainer, lp);
        initLoaders();
        return v;
    }

    @Override
    public void onDestroy() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.removeView(spinnerContainer);
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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        switch (id) {
            case CategoriesLoader.ID:
                loader = new CategoriesLoader(getActivity());
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
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static class NotesLoader extends CursorLoader {

        public NotesLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return super.loadInBackground();
        }
    }
}
