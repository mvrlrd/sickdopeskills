package ru.ibelykh.sickdopeskills.math;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Характеризация {@link Rect}: фиксирует поведение ДО того, как код начнёт двигаться.
 * Задача этих тестов — не оценить поведение, а прибить его, включая дефекты.
 */
class RectCharacterizationTest {

    /** Мир: высота ровно 1f, ширина 1f*aspect, начало координат В ЦЕНТРЕ. */
    private static Rect centeredSquare(float x, float y, float side) {
        return new Rect(x, y, side / 2f, side / 2f);
    }

    @Nested
    @DisplayName("Rect — это центр плюс полуразмеры, а не левый нижний угол плюс размеры")
    class Geometry {

        @Test
        void краяСчитаютсяОтЦентра() {
            Rect r = centeredSquare(0f, 0f, 1f);

            assertEquals(-0.5f, r.getLeft());
            assertEquals(0.5f, r.getRight());
            assertEquals(-0.5f, r.getBottom());
            assertEquals(0.5f, r.getTop());
            assertEquals(1f, r.getWidth());
            assertEquals(1f, r.getHeight());
        }

        @Test
        void установкаКраяДвигаетЦентр() {
            Rect r = centeredSquare(0f, 0f, 1f);

            r.setLeft(2f);

            assertEquals(2.5f, r.pos.x);
            assertEquals(2f, r.getLeft());
            assertEquals(3f, r.getRight());
        }

        @Test
        void установкаРазмераМеняетПолуразмеры() {
            Rect r = new Rect();

            r.setSize(4f, 6f);

            assertEquals(2f, r.getHalfWidth());
            assertEquals(3f, r.getHalfHeight());
        }
    }

    @Nested
    @DisplayName("isMe — попадание точки, границы включительно")
    class IsMe {

        private final Rect r = centeredSquare(0f, 0f, 1f);

        @Test
        void центрПопадает() {
            assertTrue(r.isMe(new Vector2(0f, 0f)));
        }

        @Test
        void уголПопадает() {
            assertTrue(r.isMe(new Vector2(0.5f, 0.5f)));
            assertTrue(r.isMe(new Vector2(-0.5f, -0.5f)));
        }

        @Test
        void чутьЗаУгломНеПопадает() {
            assertFalse(r.isMe(new Vector2(0.5001f, 0f)));
            assertFalse(r.isMe(new Vector2(0f, -0.5001f)));
        }
    }

    @Nested
    @DisplayName("isOutside — базовый предикат симметричен по всем четырём сторонам")
    class IsOutside {

        private final Rect other = centeredSquare(0f, 0f, 1f);

        @Test
        void пересекающиесяНеСнаружи() {
            assertFalse(centeredSquare(0.9f, 0f, 1f).isOutside(other));
            assertFalse(centeredSquare(0f, 0.9f, 1f).isOutside(other));
        }

        @Test
        void разошедшиесяСнаружи() {
            assertTrue(centeredSquare(1.1f, 0f, 1f).isOutside(other));
            assertTrue(centeredSquare(-1.1f, 0f, 1f).isOutside(other));
            assertTrue(centeredSquare(0f, 1.1f, 1f).isOutside(other));
            assertTrue(centeredSquare(0f, -1.1f, 1f).isOutside(other));
        }

        @Test
        @DisplayName("касание край в край — ещё НЕ снаружи: сравнения строгие")
        void касаниеНеСнаружи() {
            assertFalse(centeredSquare(1f, 0f, 1f).isOutside(other));
            assertFalse(centeredSquare(0f, -1f, 1f).isOutside(other));
        }

        @Test
        @DisplayName("предикат симметричен: a.isOutside(b) == b.isOutside(a)")
        void предикатСимметричен() {
            Rect far = centeredSquare(2f, 0f, 1f);

            assertEquals(far.isOutside(other), other.isOutside(far));
        }
    }

    @Nested
    @DisplayName("setPos сохраняет ССЫЛКУ на переданный вектор (дефект, чиним в тикете 10)")
    class SetPosAliasing {

        @Test
        @DisplayName("дефект: после setPos позиция — тот же объект, что передали")
        void позицияЭтоТотЖеОбъект() {
            Rect r = centeredSquare(0f, 0f, 1f);
            Vector2 external = new Vector2(5f, 7f);

            r.setPos(external);

            assertSame(external, r.pos);
        }

        @Test
        @DisplayName("дефект: мутация внешнего вектора молча двигает прямоугольник")
        void мутацияВнешнегоВектораДвигаетПрямоугольник() {
            Rect r = centeredSquare(0f, 0f, 1f);
            Vector2 external = new Vector2(5f, 7f);
            r.setPos(external);

            external.set(100f, 200f);

            assertEquals(99.5f, r.getLeft());
            assertEquals(200.5f, r.getTop());
        }

        @Test
        @DisplayName("set(Rect) КОПИРУЕТ — в отличие от setPos: асимметрия двух сеттеров")
        void setRectКопирует() {
            Rect r = centeredSquare(0f, 0f, 1f);
            Rect source = centeredSquare(5f, 7f, 1f);

            r.set(source);

            source.pos.set(100f, 200f);

            assertEquals(5f, r.pos.x);
            assertEquals(7f, r.pos.y);
        }
    }
}
