package ru.ibelykh.sickdopeskills.math;

import java.util.Random;

public class Rnd {

    private static final Random random = new Random();

    public Rnd() {
    }

    public static float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static int nextInt(int min, int max) {
        return (int) (Math.random() * max) + min;
    }

    public static int nextIntInterval(int min, int max) {
        return (int) (Math.random() * ((max-min)+1)) + min;
    }





}
