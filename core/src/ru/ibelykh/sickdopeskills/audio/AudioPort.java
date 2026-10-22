package ru.ibelykh.sickdopeskills.audio;

/**
 * Порт звукового сопровождения. Нужен не только ради развязки: без реализации «тишина»
 * игровую логику нельзя сконструировать в JUnit — библиотечная музыка требует аудиоустройства.
 */
public interface AudioPort {

    void play();

    void pause();

    boolean isPlaying();

    void setVolume(float volume);

    /** Реализация владеет ресурсом ОС, поэтому освобождение входит в порт. Зовёт только экран. */
    void dispose();

    /** Тишина: ничего не играет и ничем не владеет. */
    AudioPort NONE = new AudioPort() {

        @Override
        public void play() {
        }

        @Override
        public void pause() {
        }

        @Override
        public boolean isPlaying() {
            return false;
        }

        @Override
        public void setVolume(float volume) {
        }

        @Override
        public void dispose() {
        }
    };
}
