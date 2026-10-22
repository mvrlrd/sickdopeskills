package ru.ibelykh.sickdopeskills.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomSourceTest {

    /** Смесь всех трёх операций: проверяется не только воспроизводимость, но и порядок вытяжек. */
    private static List<Number> trace(RandomSource source, int steps) {
        List<Number> out = new ArrayList<Number>();
        for (int i = 0; i < steps; i++) {
            out.add(source.nextFloat(-0.7f, 0.7f));
            out.add(source.nextInt(500, 10000));
            out.add(source.nextIntInterval(5, 6));
        }
        return out;
    }

    @Nested
    @DisplayName("Фиксированный seed воспроизводим")
    class Seeded {

        @Test
        void одинSeedДаётОдинаковыеПоследовательности() {
            assertEquals(trace(new SeededRandomSource(42L), 500), trace(new SeededRandomSource(42L), 500));
        }

        @Test
        void разныеSeedДаютРазныеПоследовательности() {
            assertNotEquals(trace(new SeededRandomSource(42L), 500), trace(new SeededRandomSource(43L), 500));
        }
    }

    @Nested
    @DisplayName("Два независимых генератора: целые не сдвигают дробные")
    class TwoStreams {

        @Test
        @DisplayName("последовательность nextFloat одна и та же, сколько бы целых между ними ни взяли")
        void вытяжкиЦелыхНеСдвигаютДробные() {
            // Это и есть защита от главной ошибки порта: сложи оба потока в один Random — и спавн
            // флажков поедет, потому что FlagEmitter каждый кадр тянет и дробное, и целое.
            RandomSource clean = new SeededRandomSource(7L);
            RandomSource noisy = new SeededRandomSource(7L);

            for (int i = 0; i < 200; i++) {
                float expected = clean.nextFloat(0f, 1f);

                noisy.nextInt(0, 100);
                noisy.nextIntInterval(1, 9);
                assertEquals(expected, noisy.nextFloat(0f, 1f), "дробный поток сдвинулся на шаге " + i);
            }
        }
    }

    @Nested
    @DisplayName("Диапазоны совпадают с прибитыми в тикете 02 — у обеих реализаций")
    class Ranges {

        private static final int SAMPLES = 200_000;

        private void проверитьДиапазоны(RandomSource source) {
            int observedMin = Integer.MAX_VALUE;
            int observedMax = Integer.MIN_VALUE;
            Set<Integer> smallRange = new HashSet<Integer>();
            Set<Integer> interval = new HashSet<Integer>();

            for (int i = 0; i < SAMPLES; i++) {
                float f = source.nextFloat(-0.7f, 0.7f);
                assertTrue(f >= -0.7f && f < 0.7f, "nextFloat вне [min, max): " + f);

                int v = source.nextInt(500, 10000);
                observedMin = Math.min(observedMin, v);
                observedMax = Math.max(observedMax, v);

                int small = source.nextInt(0, 4);
                assertTrue(small >= 0 && small <= 3, "nextInt(0, 4) вне 0..3: " + small);
                smallRange.add(small);

                int inInterval = source.nextIntInterval(5, 6);
                assertTrue(inInterval == 5 || inInterval == 6, "nextIntInterval вне 5..6: " + inInterval);
                interval.add(inInterval);
            }

            // Дефект перенесён как есть: запрошенные 10000 обязаны превышаться, см. тикет 17.
            assertTrue(observedMin >= 500, "нижняя граница уехала: " + observedMin);
            assertTrue(observedMax <= 10499, "верхняя граница выше формулы: " + observedMax);
            assertTrue(observedMax >= 10000, "дефект nextInt потерян: " + observedMax);

            assertEquals(4, smallRange.size(), "nextInt(0, 4) выродился: " + smallRange);
            assertEquals(2, interval.size(), "nextIntInterval выродился: " + interval);
        }

        @Test
        void системнаяРеализация() {
            проверитьДиапазоны(new SystemRandomSource());
        }

        @Test
        void реализацияНаSeed() {
            проверитьДиапазоны(new SeededRandomSource(1L));
        }
    }

    @Nested
    @DisplayName("Rnd — фасад над системной реализацией и ничего больше")
    class Facade {

        @Test
        @DisplayName("вырожденные случаи ведут себя как у порта")
        void вырожденныеСлучаи() {
            assertEquals(0.5f, Rnd.nextFloat(0.5f, 0.5f));
            for (int i = 0; i < 1000; i++) {
                assertEquals(7, Rnd.nextIntInterval(7, 7));
            }
        }
    }
}
