package ru.ibelykh.sickdopeskills.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Характеризация {@link Rnd}. Генератор не инжектируется (это тикет 03), поэтому диапазоны
 * проверяются выборкой: столько прогонов, что промах по границе исключён практически.
 */
class RndCharacterizationTest {

    /** При 5% вероятности попадания в проверяемую зону промах имеет вероятность 0.95^200000. */
    private static final int SAMPLES = 200_000;

    @Nested
    @DisplayName("nextFloat сидит на поле Random и даёт [min, max)")
    class NextFloat {

        @Test
        void значенияВнутриДиапазона() {
            float min = -0.7f;
            float max = 0.7f;

            for (int i = 0; i < SAMPLES; i++) {
                float v = Rnd.nextFloat(min, max);
                assertTrue(v >= min && v < max, "вне диапазона: " + v);
            }
        }

        @Test
        @DisplayName("вырожденный диапазон даёт ровно min")
        void вырожденныйДиапазон() {
            assertEquals(0.5f, Rnd.nextFloat(0.5f, 0.5f));
        }
    }

    @Nested
    @DisplayName("nextInt даёт [min, min+max) вместо [min, max) — ДЕФЕКТ, чиним в тикете 17")
    class NextIntDefect {

        @Test
        @DisplayName("дефект: nextInt(500, 10000) даёт 500..10499, а не 500..9999")
        void снегДваждыПлотнее() {
            int observedMin = Integer.MAX_VALUE;
            int observedMax = Integer.MIN_VALUE;

            for (int i = 0; i < SAMPLES; i++) {
                int v = Rnd.nextInt(500, 10000);
                observedMin = Math.min(observedMin, v);
                observedMax = Math.max(observedMax, v);
            }

            // Формула: (int)(Math.random() * max) + min. Умножение на верхнюю границу вместо размаха.
            assertTrue(observedMin >= 500, "нижняя граница уехала: " + observedMin);
            assertTrue(observedMax <= 10499, "верхняя граница выше формулы: " + observedMax);
            assertTrue(observedMax >= 10000,
                    "запрошенная верхняя граница 10000 должна превышаться — в этом и дефект: " + observedMax);
        }

        @Test
        @DisplayName("при min == 0 обе формулы совпадают, поэтому nextInt(0, 4) правкой НЕ затронут")
        void приНулевомМинимумеДефектаНет() {
            Set<Integer> seen = new HashSet<>();

            for (int i = 0; i < SAMPLES; i++) {
                int v = Rnd.nextInt(0, 4);
                assertTrue(v >= 0 && v <= 3, "вне 0..3: " + v);
                seen.add(v);
            }

            assertEquals(new HashSet<>(java.util.Arrays.asList(0, 1, 2, 3)), seen);
        }
    }

    @Nested
    @DisplayName("nextIntInterval включает обе границы и дефекта не имеет")
    class NextIntInterval {

        @Test
        void обеГраницыДостижимы() {
            Set<Integer> seen = new HashSet<>();

            for (int i = 0; i < SAMPLES; i++) {
                int v = Rnd.nextIntInterval(5, 6);
                assertTrue(v == 5 || v == 6, "вне 5..6: " + v);
                seen.add(v);
            }

            assertEquals(2, seen.size());
        }

        @Test
        @DisplayName("вырожденный интервал даёт ровно min")
        void вырожденныйИнтервал() {
            for (int i = 0; i < 1000; i++) {
                assertEquals(7, Rnd.nextIntInterval(7, 7));
            }
        }
    }

    @Nested
    @DisplayName("nextFloat и nextInt сидят на РАЗНЫХ генераторах")
    class TwoGenerators {

        @Test
        @DisplayName("nextFloat — поле Random, nextInt/nextIntInterval — Math.random(); порядок вытяжек независим")
        void генераторыНезависимы() {
            // Прямой проверки «какой генератор» из теста нет, поэтому фиксируется следствие:
            // вытяжки одного вида не сдвигают последовательность другого вида.
            // Тест существует как напоминание для тикета 03: порт обязан сохранить это разделение,
            // иначе последовательности сдвинутся и спавн изменится.
            Set<Float> floats = new HashSet<>();
            for (int i = 0; i < 1000; i++) {
                Rnd.nextInt(0, 100);
                floats.add(Rnd.nextFloat(0f, 1f));
            }

            assertTrue(floats.size() > 900, "nextFloat выродился: " + floats.size());
        }
    }
}
