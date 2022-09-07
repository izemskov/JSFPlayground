package ru.develgame.jsfgame.domain;

public enum PersonType {
    PERSON_TYPE1(100, 50, 8),
    PERSON_TYPE2(100, 50, 8),
    PERSON_TYPE3(40, 40, 10),
    PERSON_TYPE4(60, 80, 4);

    private final int width;
    private final int height;
    private final int maxFrame;

    PersonType(int width, int height, int maxFrame) {
        this.width = width;
        this.height = height;
        this.maxFrame = maxFrame;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxFrame() {
        return maxFrame;
    }
}
