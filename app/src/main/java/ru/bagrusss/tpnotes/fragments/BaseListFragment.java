package ru.bagrusss.tpnotes.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.bagrusss.tpnotes.R;

/**
 * Created by bagrusss.
 */
public class BaseListFragment extends Fragment {

    protected Class<? extends AppCompatActivity> mForStart;
    protected FloatingActionButton mFab;
    protected ListView mListView;

    public BaseListFragment(Class<? extends AppCompatActivity> activity) {
        mForStart = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_fab, container, false);
        mFab = (FloatingActionButton) v.findViewById(R.id.add_button);
        if (mFab != null) {
            mFab.setOnClickListener(view -> startActivity(new Intent(getActivity(), mForStart)));
        }
        mListView = (ListView) v.findViewById(R.id.list_view);
        return v;
    }
}
