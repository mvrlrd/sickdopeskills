package ru.ibelykh.sickdopeskills.math;

import java.util.Random;

/** Системная случайность: ровно те же два генератора, что были в {@link Rnd} до выноса порта. */
public class SystemRandomSource extends BaseRandomSource {

    private final Random random = new Random();

    @Override
    protected float rawFloat() {
        return random.nextFloat();
    }

    @Override
    protected double rawDouble() {
        return Math.random();
    }
}
