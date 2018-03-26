package su.css3.openwithelementum;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import java.util.List;

import su.css3.openwithelementum.utils.AppUtils;
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
            final Context context = preferenceScreen.getContext();

            final EditTextPreference hostPref = (EditTextPreference) preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_HOST);
            final EditTextPreference timeoutPref = (EditTextPreference) preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_TIMEOUT);
            final ListPreference applicationPref = (ListPreference) preferenceScreen.findPreference(PreferencesUtils.KEY_PREF_KODI_APP);

            // Проверка хоста
            hostPref.setOnPreferenceChangeListener((preference, newValue) -> newValue.toString().length() >= 3);

            // Проверка таймаута
            timeoutPref.setOnPreferenceChangeListener((preference, newValue) -> {
                int timeout = 0;
                try {
                    timeout = Integer.parseInt(newValue.toString());
                } catch (NumberFormatException ignored) {}
                return timeout >= 3 && timeout <= 60;
            });

            // Проверка приложения
            List<CharSequence[]> kodiPackages = AppUtils.getKodiPackages(context);
            CharSequence[] entryValues = new String[kodiPackages.size() + 1];
            CharSequence[] entries = new String[kodiPackages.size() + 1];

            entryValues[0] = "";
            entries[0] = context.getResources().getString(R.string.pref_network_host);

            if (!kodiPackages.isEmpty()) {
                for (int i = 0; i < kodiPackages.size(); i++) {
                    CharSequence[] kodiPackage = kodiPackages.get(i);
                    entryValues[i + 1] = kodiPackage[0];
                    entries[i + 1] = kodiPackage[1] + " (" + kodiPackage[0] + ")";
                }
            }

            applicationPref.setEntries(entries);
            applicationPref.setEntryValues(entryValues);

            String appName = applicationPref.getValue();
            if (appName == null || appName.isEmpty() || !AppUtils.isAppInstalled(context, appName)) {
                applicationPref.setValueIndex(0);
                hostPref.setEnabled(true);
            }

            applicationPref.setOnPreferenceChangeListener((preference, newValue) -> {
                hostPref.setEnabled(newValue.equals(""));
                return true;
            });
        }
    }

}
