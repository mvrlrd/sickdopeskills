package ru.ibelykh.sickdopeskills.math;

/**
 * Статический фасад над {@link RandomSource}: оставлен, чтобы вызывающие не менялись. Новый код,
 * которому нужна детерминированность, получает {@link RandomSource} инжекцией, а не через этот
 * класс.
 */
public class Rnd {

    private static final RandomSource SOURCE = new SystemRandomSource();

    public Rnd() {
    }

    public static float nextFloat(float min, float max) {
        return SOURCE.nextFloat(min, max);
    }

    public static int nextInt(int min, int max) {
        return SOURCE.nextInt(min, max);
    }

    public static int nextIntInterval(int min, int max) {
        return SOURCE.nextIntInterval(min, max);
    }
}
