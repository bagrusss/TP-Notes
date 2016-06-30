package ru.bagrusss.tpnotes.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.EditActivity;
import ru.bagrusss.tpnotes.activities.SettingsActivity;

/**
 * Created by bagrusss.
 */

public class NotesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_button);
        if (fab != null) {
            fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), EditActivity.class)));
        }
        return v;
    }
}
