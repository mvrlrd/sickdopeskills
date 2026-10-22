package ru.ibelykh.sickdopeskills.math;

/**
 * Источник случайности. Инжектируется, чтобы спавн и таблицу сложности можно было прогнать
 * детерминированно: пороги на 10/20/30/40 очках руками набрать слишком долго.
 */
public interface RandomSource {

    /** Дробное из {@code [min, max)}. */
    float nextFloat(float min, float max);

    /**
     * Целое. Сейчас даёт {@code [min, min + max)} вместо {@code [min, max)} — дефект, чиним
     * в тикете 17. Здесь поведение перенесено как есть.
     */
    int nextInt(int min, int max);

    /** Целое из {@code [min, max]}, обе границы включительно. */
    int nextIntInterval(int min, int max);
}
