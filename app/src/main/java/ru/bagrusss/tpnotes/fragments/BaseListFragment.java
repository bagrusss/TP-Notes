package ru.bagrusss.tpnotes.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.EditCategoriesActivity;
import ru.bagrusss.tpnotes.activities.EditNotesActivity;
import ru.bagrusss.tpnotes.adapters.CategoryAdapter;
import ru.bagrusss.tpnotes.data.HelperDB;

/**
 * Created by bagrusss.
 */
public abstract class BaseListFragment extends Fragment {

    protected FloatingActionButton mFab;
    protected ListView mListView;
    public static final String ACTIVITY_CODE = "ACTIVITY_CODE";
    protected CategoryAdapter mCategoryAdapter;

    public BaseListFragment() {

    }

    protected void loadCategories(LoaderManager.LoaderCallbacks<?> callbacks) {
        getLoaderManager().initLoader(CategoriesLoader.ID, null, callbacks);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_fab, container, false);
        mFab = (FloatingActionButton) v.findViewById(R.id.add_button);
        if (mFab != null) {
            Bundle args = getArguments();
            mFab.setOnClickListener(view ->
                    startActivity(new Intent(getActivity(),
                            args.getInt(ACTIVITY_CODE, EditNotesActivity.CODE) == EditNotesActivity.CODE ?
                                    EditNotesActivity.class : EditCategoriesActivity.class)));
        }
        mListView = (ListView) v.findViewById(R.id.list_view);
        return v;
    }

    protected static class CategoriesLoader extends CursorLoader {

        public static final int ID = 100;

        public CategoriesLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return HelperDB.getInstance(getContext()).allCategories();
        }
    }

}
