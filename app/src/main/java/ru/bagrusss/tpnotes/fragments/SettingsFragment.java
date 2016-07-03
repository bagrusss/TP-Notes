package ru.bagrusss.tpnotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.activities.NotesActivity;

/**
 * Created by bagrusss.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_app_settings);
        findPreference(getString(R.string.key_theme))
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    if (preference.getKey().equals(getString(R.string.key_theme))) {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), NotesActivity.class));
                        return true;
                    }
                    return false;
                });
        /*
        EditText pass = ((EditTextPreference) findPreference(getString(R.string.key_password)))
                .getEditText();
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(8);
        pass.setFilters(filters);*/
    }
}
