package ru.ibelykh.sickdopeskills.store;

/**
 * Общая часть реализаций, см. {@link CachedScoreStore}. Здесь дедупликация особенно важна:
 * вызывающий ставит настройку каждый кадр, а не по событию.
 */
public abstract class CachedSettingsStore implements SettingsStore {

    private boolean loaded;
    private boolean cached;

    @Override
    public final boolean isSoundOff() {
        if (!loaded) {
            cached = read();
            loaded = true;
        }
        return cached;
    }

    @Override
    public final void setSoundOff(boolean soundOff) {
        if (isSoundOff() == soundOff) {
            return;
        }
        cached = soundOff;
        write(soundOff);
    }

    protected abstract boolean read();

    protected abstract void write(boolean soundOff);
}
