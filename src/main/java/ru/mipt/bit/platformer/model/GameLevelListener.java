package ru.mipt.bit.platformer.model;

/** Port: tells the outside world which objects appeared in the level and which left it. */
public interface GameLevelListener {

    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
