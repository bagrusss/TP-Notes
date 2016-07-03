package ru.bagrusss.tpnotes.fragments;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.BaseActivity;
import ru.bagrusss.tpnotes.activities.EditCategoriesActivity;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;
import ru.bagrusss.tpnotes.eventbus.Message;
import ru.bagrusss.tpnotes.services.ServiceHelper;

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
        EventBus.getDefault().register(this);
        return v;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Message msg) {
        if (msg.reqCode == REQUEST_CODE && msg.status == Message.OK)
            getLoaderManager().getLoader(CategoriesLoader.ID).forceLoad();
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
        editCategory(view, id);
    }

    private void editCategory(View view, long id) {
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                if (info.id > 2) {
                    CategoryAdapter.ViewHolder holder = (CategoryAdapter.ViewHolder) info.targetView.getTag();
                    showDeleteDialog(holder);
                }
                break;
            case 0:
                editCategory(info.targetView, info.id);
                break;
            default:
                return false;
        }
        return true;
    }

    private void showDeleteDialog(CategoryAdapter.ViewHolder holder) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.delete_notes_message)
                .setTitle(R.string.delete_title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    ServiceHelper.deleteCategory(getActivity(), holder.text.getText().toString());
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which1) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

}
