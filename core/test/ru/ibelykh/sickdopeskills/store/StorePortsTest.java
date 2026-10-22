package ru.ibelykh.sickdopeskills.store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorePortsTest {

    /** 10 секунд игры при 60 FPS — столько записей делал старый код на каждое поле. */
    private static final int FRAMES = 600;

    @Nested
    @DisplayName("Рекорд")
    class Score {

        @Test
        void сохранённоеЗначениеЧитаетсяОбратно() {
            InMemoryScoreStore store = new InMemoryScoreStore(5);

            store.saveHighScore(17);

            assertEquals(17, store.highScore());
            assertEquals(1, store.writes());
        }

        @Test
        @DisplayName("повторное сохранение того же числа не пишет в хранилище")
        void повторнаяЗаписьНеИдёт() {
            InMemoryScoreStore store = new InMemoryScoreStore(5);

            store.saveHighScore(17);
            for (int i = 0; i < FRAMES; i++) {
                store.saveHighScore(17);
            }

            assertEquals(1, store.writes());
        }

        @Test
        @DisplayName("«корона» появляется в кадре, когда очки СРАВНЯЛИСЬ с рекордом, и дальше не гаснет")
        void коронаПоявляетсяВКадреДостиженияРекорда() {
            // Воспроизводится правило из игрового цикла: сравнение с ХРАНИЛИЩЕМ, затем сохранение,
            // затем highScore = points. Корона рисуется при points == highScore && points != 0.
            InMemoryScoreStore store = new InMemoryScoreStore(5);
            int highScore = store.highScore();

            boolean[] crown = new boolean[9];
            for (int points = 1; points <= 8; points++) {
                if (points > store.highScore()) {
                    store.saveHighScore(points);
                    highScore = points;
                }
                crown[points] = (points == highScore) && (points != 0);
            }

            // Пока очков меньше рекорда — короны нет.
            assertFalse(crown[1]);
            assertFalse(crown[4]);
            // 5 очков — рекорд ещё НЕ побит, он только СРАВНЯЛСЯ, но корона уже рисуется:
            // условие отрисовки сравнивает на равенство. Это текущее поведение, не дефект.
            assertTrue(crown[5]);
            assertTrue(crown[6]);
            assertTrue(crown[8]);
            // Запись же идёт только там, где рекорд реально превышен: 6, 7 и 8 очков.
            assertEquals(3, store.writes(), "на 5 очках записи быть не должно");
        }

        @Test
        @DisplayName("на чистых данных корона появляется с первого очка")
        void наЧистыхДанныхКоронаСПервогоОчка() {
            InMemoryScoreStore store = new InMemoryScoreStore();

            assertTrue(1 > store.highScore());
            store.saveHighScore(1);

            assertEquals(1, store.highScore());
        }
    }

    @Nested
    @DisplayName("Настройки звука")
    class Settings {

        @Test
        @DisplayName("установка того же значения каждый кадр не пишет в хранилище ни разу")
        void записиНаКаждыйТикНет() {
            InMemorySettingsStore store = new InMemorySettingsStore(false);

            for (int i = 0; i < FRAMES; i++) {
                store.setSoundOff(false);
            }

            assertEquals(0, store.writes());
            assertFalse(store.isSoundOff());
        }

        @Test
        @DisplayName("на каждое переключение приходится ровно одна запись")
        void однаЗаписьНаПереключение() {
            InMemorySettingsStore store = new InMemorySettingsStore(false);

            for (int i = 0; i < FRAMES; i++) {
                store.setSoundOff(true);
            }
            assertEquals(1, store.writes());
            assertTrue(store.isSoundOff());

            for (int i = 0; i < FRAMES; i++) {
                store.setSoundOff(false);
            }
            assertEquals(2, store.writes());
            assertFalse(store.isSoundOff());
        }
    }
}
