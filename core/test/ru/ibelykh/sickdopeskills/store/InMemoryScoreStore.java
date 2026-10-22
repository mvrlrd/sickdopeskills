package ru.ibelykh.sickdopeskills.store;

/** Фейк рекорда: считает именно записи в «хранилище», а не вызовы порта. */
public class InMemoryScoreStore extends CachedScoreStore {

    private int stored;
    private int writes;

    public InMemoryScoreStore() {
        this(0);
    }

    public InMemoryScoreStore(int stored) {
        this.stored = stored;
    }

    public int writes() {
        return writes;
    }

    @Override
    protected int read() {
        return stored;
    }

    @Override
    protected void write(int points) {
        stored = points;
        writes++;
    }
}
