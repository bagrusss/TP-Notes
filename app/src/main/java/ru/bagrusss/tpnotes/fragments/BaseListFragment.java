package ru.bagrusss.tpnotes.fragments;

import android.app.Fragment;
import android.content.Intent;
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

/**
 * Created by bagrusss.
 */
public class BaseListFragment extends Fragment {

    protected FloatingActionButton mFab;
    protected ListView mListView;
    public static final String ACTIVITY_CODE = "ACTIVITY_CODE";

    public BaseListFragment() {

    }

    public static BaseListFragment newInstance(int activityCode) {
        Bundle args = new Bundle();
        args.putInt(ACTIVITY_CODE, activityCode);
        BaseListFragment fragment = new BaseListFragment();
        fragment.setArguments(args);
        return fragment;
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

}
