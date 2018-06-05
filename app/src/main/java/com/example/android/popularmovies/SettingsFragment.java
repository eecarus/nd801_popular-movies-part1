package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);

        updatePreferenceSummaries();
    }

    // ---------------------------------------------------------------------------------------------
    // Implementations for PreferenceSummary
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceSummaries();
    }

    private void updatePreferenceSummaries() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
            Preference preference = preferenceScreen.getPreference(i);
            updatePreferenceSummary(preference);
        }
    }

    private void updatePreferenceSummary(Preference preference) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            String value = listPreference.getValue();
            int selectedIndex = listPreference.findIndexOfValue(value);
            if (selectedIndex >= 0) {
                preference.setSummary(((ListPreference) preference).getEntries()[selectedIndex]);
            }
        }
    }

}
