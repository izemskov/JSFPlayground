package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Direction;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("imageTestBean")
@SessionScoped
public class ImageTestBean implements Serializable {
    private int currentFrame = 1;

    private int imageTop = 200;

    private int imageLeft = 200;

    private Direction direction = Direction.DOWN;

    private boolean moving = false;

    public String loadImage() {
        String image = "/images/walk_" + direction.toString().toLowerCase() + currentFrame + ".png";
        if (moving)
            currentFrame++;
        if (currentFrame > 8)
            currentFrame = 1;
        return image;
    }

    public String getTopPosition() {
        if (moving) {
            if (direction == Direction.DOWN)
                imageTop = imageTop + 2;
            if (direction == Direction.UP && imageTop > 0)
                imageTop = imageTop - 2;
        }

        return Integer.toString(imageTop);
    }

    public String getLeftPosition() {
        if (moving) {
            if (direction == Direction.LEFT && imageLeft > 0)
                imageLeft = imageLeft - 2;
            if (direction == Direction.RIGHT)
                imageLeft = imageLeft + 2;
        }

        return Integer.toString(imageLeft);
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void stop() {
        moving = false;
    }

    public void left() {
        direction = Direction.LEFT;
        currentFrame = 1;
        moving = true;
    }

    public void right() {
        direction = Direction.RIGHT;
        currentFrame = 1;
        moving = true;
    }

    public void up() {
        direction = Direction.UP;
        currentFrame = 1;
        moving = true;
    }

    public void down() {
        direction = Direction.DOWN;
        currentFrame = 1;
        moving = true;
    }
}
