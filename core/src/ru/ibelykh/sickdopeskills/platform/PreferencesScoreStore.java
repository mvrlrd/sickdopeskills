package ru.ibelykh.sickdopeskills.platform;

import com.badlogic.gdx.Preferences;
import ru.ibelykh.sickdopeskills.store.CachedScoreStore;

public class PreferencesScoreStore extends CachedScoreStore {

    private final Preferences prefs = GdxPreferences.get();

    @Override
    protected int read() {
        return prefs.getInteger(GdxPreferences.HIGH_SCORE);
    }

    @Override
    protected void write(int points) {
        prefs.putInteger(GdxPreferences.HIGH_SCORE, points);
        prefs.flush();
    }
}
