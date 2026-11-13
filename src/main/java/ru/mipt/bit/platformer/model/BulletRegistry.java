package ru.mipt.bit.platformer.model;

/** Port: lets a tank hand a new bullet over to the level. */
public interface BulletRegistry {

    void register(Bullet bullet);
}
