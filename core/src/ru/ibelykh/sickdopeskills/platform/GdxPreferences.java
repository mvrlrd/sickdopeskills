package ru.ibelykh.sickdopeskills.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Единственное место в проекте, которое знает имя платформенного хранилища и ключи в нём.
 * {@code Gdx.app.getPreferences} кеширует экземпляр по имени, поэтому оба стора работают с одним
 * и тем же файлом настроек.
 */
final class GdxPreferences {

    static final String HIGH_SCORE = "pts";
    static final String SOUND_OFF = "soundOff";

    private static final String NAME = "high score and sound";

    private GdxPreferences() {
    }

    static Preferences get() {
        return Gdx.app.getPreferences(NAME);
    }
}
