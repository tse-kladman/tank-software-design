package ru.mipt.bit.platformer.model.level;

/** Port: where the level layout comes from. */
public interface LevelSource {

    LevelLayout load();
}
