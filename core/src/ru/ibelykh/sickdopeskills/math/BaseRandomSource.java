package ru.ibelykh.sickdopeskills.math;

/**
 * Формулы лежат здесь в единственном экземпляре, реализации отличаются только тем, откуда берут
 * сырые числа. Формулы перенесены из старого {@link Rnd} символ в символ, вместе с дефектом
 * {@code nextInt} — тикет 17 правит одно место, а не два.
 * <p>
 * Два сырых метода вместо одного — не прихоть: {@code nextFloat} сидел на экземпляре
 * {@code Random}, а {@code nextInt}/{@code nextIntInterval} на {@code Math.random()}, то есть на
 * ДВУХ независимых генераторах. Слить их в один — значит сдвинуть последовательность вытяжек
 * и изменить спавн.
 */
abstract class BaseRandomSource implements RandomSource {

    /** Сырое дробное из {@code [0, 1)} первого генератора — на нём сидит только {@code nextFloat}. */
    protected abstract float rawFloat();

    /** Сырое дробное из {@code [0, 1)} второго генератора — на нём вся целочисленная генерация. */
    protected abstract double rawDouble();

    @Override
    public float nextFloat(float min, float max) {
        return rawFloat() * (max - min) + min;
    }

    @Override
    public int nextInt(int min, int max) {
        return (int) (rawDouble() * max) + min;
    }

    @Override
    public int nextIntInterval(int min, int max) {
        return (int) (rawDouble() * ((max - min) + 1)) + min;
    }
}
