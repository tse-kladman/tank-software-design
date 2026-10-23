package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Gdx;

import java.util.Arrays;
import java.util.List;

public final class GdxTextFiles {

    private GdxTextFiles() {
    }

    public static List<String> readLines(String fileName) {
        return Arrays.asList(Gdx.files.internal(fileName).readString().split("\r?\n"));
    }
}
