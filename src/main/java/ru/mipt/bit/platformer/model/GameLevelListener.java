package ru.mipt.bit.platformer.model;

public interface GameLevelListener {

    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
