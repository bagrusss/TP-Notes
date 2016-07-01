package ru.bagrusss.tpnotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.bagrusss.tpnotes.R;

/**
 * Created by bagrusss.
 */

public class NotesFragment extends BaseListFragment {

    public NotesFragment(Class<? extends AppCompatActivity> activity) {
        super(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_fab, container, false);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_button);
        if (fab != null) {
            fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), mForStart)));
        }
        return v;
    }
}
