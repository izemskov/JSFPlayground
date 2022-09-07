package ru.develgame.jsfgame.location;

import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Ilya Zemskov
 */
@Singleton
@Startup
public class LocationMap {
    public boolean isPass(int x, int y) {
        return true;
    }
}
