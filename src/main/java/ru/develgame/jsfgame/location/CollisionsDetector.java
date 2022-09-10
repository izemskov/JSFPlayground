package ru.develgame.jsfgame.location;

import ru.develgame.jsfgame.domain.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class CollisionsDetector {
    public boolean checkCollision(Person a, Person b) {
        boolean aLeftOfB = a.getImageLeft() + a.getWidth() < b.getImageLeft();
        boolean aRightOfB = a.getImageLeft() > b.getImageLeft() + b.getWidth();
        boolean aAboveB = a.getImageTop() > b.getImageTop() + b.getHeight();
        boolean aBelowB = a.getImageTop() + a.getHeight() < b.getImageTop();

        return !(aLeftOfB || aRightOfB || aAboveB || aBelowB);
    }
}
