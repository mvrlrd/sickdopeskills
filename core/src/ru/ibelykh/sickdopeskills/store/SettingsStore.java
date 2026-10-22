package ru.ibelykh.sickdopeskills.store;

/** Хранилище настроек. Пока в нём одна настройка — выключен ли звук. */
public interface SettingsStore {

    boolean isSoundOff();

    void setSoundOff(boolean soundOff);
}
