package ru.ibelykh.sickdopeskills.store;

/** Фейк настроек: считает именно записи в «хранилище», а не вызовы порта. */
public class InMemorySettingsStore extends CachedSettingsStore {

    private boolean stored;
    private int writes;

    public InMemorySettingsStore() {
        this(false);
    }

    public InMemorySettingsStore(boolean stored) {
        this.stored = stored;
    }

    public int writes() {
        return writes;
    }

    @Override
    protected boolean read() {
        return stored;
    }

    @Override
    protected void write(boolean soundOff) {
        stored = soundOff;
        writes++;
    }
}
