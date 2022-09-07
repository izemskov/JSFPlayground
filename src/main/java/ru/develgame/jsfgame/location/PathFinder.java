package ru.develgame.jsfgame.location;

import ru.develgame.jsfgame.domain.Direction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Ilya Zemskov
 */
@Named
@ApplicationScoped
public class PathFinder {
    public Direction getNextDirection(int srcX, int srcY, int dstX, int dstY) {
        if (Math.abs(dstY - srcY) > Math.abs(dstX - srcX)) {
            if (dstY > srcY)
                return Direction.DOWN;
            else
                return Direction.UP;
        }
        else {
            if (dstX > srcX)
                return Direction.RIGHT;
            else
                return Direction.LEFT;
        }
    }
}
