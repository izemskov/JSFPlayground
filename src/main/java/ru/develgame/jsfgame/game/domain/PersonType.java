package ru.develgame.jsfgame.game.domain;

public enum PersonType {
    PERSON_TYPE1(50, 115, 8, 0),
    PERSON_TYPE2(50, 115, 8, 0),
    PERSON_TYPE3(40, 40, 10, 0),
    PERSON_TYPE4(60, 80, 4, 5);

    private final int width;
    private final int height;
    private final int maxFrame;

    private final int maxAttackFrame;

    PersonType(int width, int height, int maxFrame, int maxAttackFrame) {
        this.width = width;
        this.height = height;
        this.maxFrame = maxFrame;
        this.maxAttackFrame = maxAttackFrame;
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

    public int getMaxAttackFrame() {
        return maxAttackFrame;
    }
}
