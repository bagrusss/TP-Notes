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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.BaseActivity;
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

    protected void loadCategories(LoaderManager.LoaderCallbacks<?> callbacks) {
        getLoaderManager().initLoader(CategoriesLoader.ID, null, callbacks);
    }

    abstract int getReqCode();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_fab, container, false);
        mFab = (FloatingActionButton) v.findViewById(R.id.add_button);
        if (mFab != null) {
            Bundle args = getArguments();
            mFab.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(),
                        args.getInt(ACTIVITY_CODE, EditNotesActivity.REQUEST_CODE) ==
                                EditNotesActivity.REQUEST_CODE ?
                                EditNotesActivity.class : EditCategoriesActivity.class);
                intent.setAction(BaseActivity.ACTION_NEW);
                startActivityForResult(intent, getReqCode());
            });
        }
        mListView = (ListView) v.findViewById(R.id.list_view);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static class CategoriesLoader extends CursorLoader {

        public static final int ID = 100;
        private boolean all;

        public CategoriesLoader(Context context, boolean showAll) {
            super(context);
            all = showAll;
        }

        @Override
        public Cursor loadInBackground() {
            return all ? HelperDB.getInstance(getContext()).allCategories() :
                    HelperDB.getInstance(getContext()).notAllCategories();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_view) {
            String[] items = getResources().getStringArray(R.array.menu_list);
            menu.setHeaderTitle(R.string.context_menu);
            for (int i = 0; i < items.length; i++) {
                menu.add(Menu.NONE, i, i, items[i]);
            }
        }
    }

}
