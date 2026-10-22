package ru.ibelykh.sickdopeskills.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import ru.ibelykh.sickdopeskills.audio.AudioPort;

/** Единственное место в проекте, которое знает про библиотечную музыку. */
public class GdxMusicAudio implements AudioPort {

    private final Music music;

    public GdxMusicAudio(String path) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(path));
        this.music.setLooping(true);
    }

    @Override
    public void play() {
        music.play();
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public boolean isPlaying() {
        return music.isPlaying();
    }

    @Override
    public void setVolume(float volume) {
        music.setVolume(volume);
    }

    @Override
    public void dispose() {
        music.dispose();
    }
}
