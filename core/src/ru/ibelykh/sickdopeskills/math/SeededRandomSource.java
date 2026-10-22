package ru.ibelykh.sickdopeskills.math;

import java.util.Random;

/**
 * Случайность на фиксированном seed: один и тот же seed даёт одну и ту же последовательность.
 * {@code Math.random()} засеять нельзя, поэтому второй генератор здесь — отдельный
 * {@code Random}; разделение на два независимых потока сохранено, см. {@link BaseRandomSource}.
 */
public class SeededRandomSource extends BaseRandomSource {

    private final Random floatRandom;
    private final Random intRandom;

    public SeededRandomSource(long seed) {
        // Оба потока выводятся из одного seed, но не совпадают: засей их одним числом — и целые
        // пошли бы строго параллельно дробным, чего в системной реализации нет.
        Random seeds = new Random(seed);
        this.floatRandom = new Random(seeds.nextLong());
        this.intRandom = new Random(seeds.nextLong());
    }

    @Override
    protected float rawFloat() {
        return floatRandom.nextFloat();
    }

    @Override
    protected double rawDouble() {
        return intRandom.nextDouble();
    }
}
