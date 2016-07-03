package ru.bagrusss.tpnotes.fragments;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.BaseActivity;
import ru.bagrusss.tpnotes.activities.EditCategoriesActivity;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;

/**
 * Created by bagrusss.
 */
public class CategoriesFragment extends BaseListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public static final int REQUEST_CODE = 305;

    @Override
    int getReqCode() {
        return REQUEST_CODE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mCategoryAdapter = new CategoryAdapter(getActivity(), null, R.layout.item_category);
        mListView.setAdapter(mCategoryAdapter);
        mListView.setOnItemClickListener(this);
        loadCategories(this);
        return v;
    }

    public static CategoriesFragment newInstance(int activityCode) {
        Bundle args = new Bundle();
        args.putInt(ACTIVITY_CODE, activityCode);
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CategoriesLoader(getActivity(), false);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        mCategoryAdapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), EditCategoriesActivity.class);
        intent.setAction(BaseActivity.ACTION_EDIT);
        CategoryAdapter.ViewHolder holder = (CategoryAdapter.ViewHolder) view.getTag();
        intent.putExtra(BaseActivity.KEY_CATEGORY, holder.text.getText().toString());
        intent.putExtra(BaseActivity.KEY_COLOR, holder.colorVal);
        intent.putExtra(BaseActivity.KEY_ID, id);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getLoaderManager().getLoader(CategoriesLoader.ID).forceLoad();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
