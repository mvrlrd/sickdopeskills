package ru.ibelykh.sickdopeskills.store;

/** Хранилище рекорда. Игровой цикл знает только этот интерфейс. */
public interface ScoreStore {

    int highScore();

    /**
     * Сохраняет рекорд. Сразу после вызова {@link #highScore()} обязан вернуть новое значение:
     * на этом стоит момент появления «короны» — она рисуется, когда очки равны рекорду.
     */
    void saveHighScore(int points);
}
