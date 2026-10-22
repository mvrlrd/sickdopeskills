package ru.ibelykh.sickdopeskills.store;

/**
 * Общая часть реализаций: значение читается из хранилища один раз, запись идёт только при
 * изменении. Логика лежит здесь, а не в платформенной реализации, чтобы её проверял тест на
 * in-memory фейке — иначе проверялся бы фейк, а не то, что поедет в игру.
 */
public abstract class CachedScoreStore implements ScoreStore {

    private boolean loaded;
    private int cached;

    @Override
    public final int highScore() {
        if (!loaded) {
            cached = read();
            loaded = true;
        }
        return cached;
    }

    @Override
    public final void saveHighScore(int points) {
        if (highScore() == points) {
            return;
        }
        cached = points;
        write(points);
    }

    protected abstract int read();

    protected abstract void write(int points);
}
