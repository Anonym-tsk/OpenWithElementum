package su.css3.openwithelementum;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import su.css3.openwithelementum.utils.PreferencesUtils;

public class PreferencesActivity extends PreferenceActivity {

    private MyPreferenceFragment fragment = new MyPreferenceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final PreferenceScreen preferenceScreen = getPreferenceScreen();

            boolean isLocally = getPreferenceManager().getSharedPreferences().getBoolean(PreferencesUtils.KEY_PREF_KODI_LOCALLY, true);
            preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_HOST).setEnabled(!isLocally);

            Preference locallyPref = preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_LOCALLY);
            locallyPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isLocally = newValue.toString().equals("true");
                    preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_HOST).setEnabled(!isLocally);
                    return true;
                }
            });

            EditTextPreference timeoutPref = (EditTextPreference) preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_TIMEOUT);
            timeoutPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int timeout = 0;
                    try {
                        timeout = Integer.parseInt(newValue.toString());
                    } catch (NumberFormatException ignored) {}
                    return timeout >= 3 && timeout <= 60;
                }
            });

            EditTextPreference hostPref = (EditTextPreference) preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_HOST);
            hostPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    return newValue.toString().length() >= 3;
                }
            });
        }
    }

}
