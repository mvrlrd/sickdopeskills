package ru.ibelykh.sickdopeskills.platform;

import com.badlogic.gdx.Preferences;
import ru.ibelykh.sickdopeskills.store.CachedSettingsStore;

public class PreferencesSettingsStore extends CachedSettingsStore {

    private final Preferences prefs = GdxPreferences.get();

    @Override
    protected boolean read() {
        return prefs.getBoolean(GdxPreferences.SOUND_OFF);
    }

    @Override
    protected void write(boolean soundOff) {
        prefs.putBoolean(GdxPreferences.SOUND_OFF, soundOff);
        prefs.flush();
    }
}
