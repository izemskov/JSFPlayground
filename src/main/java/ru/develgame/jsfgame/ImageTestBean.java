package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Direction;
import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.domain.PersonType;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("imageTestBean")
@SessionScoped
public class ImageTestBean implements Serializable {
    @Inject
    private Person person;

    public String loadImage() {
        if (person == null)
            return "";

        String image = "/images/" + person.getPersonType().toString() + "/walk_" + person.getDirection().toString().toLowerCase()
                + person.getCurrentFrame() + ".png";
        if (person.isMoving())
            person.setCurrentFrame(person.getCurrentFrame() + 1);
        if (person.getCurrentFrame() > 8)
            person.setCurrentFrame(1);
        return image;
    }

    public String getTopPosition() {
        if (person == null)
            return "0";

        if (person.isMoving()) {
            if (person.getDirection() == Direction.DOWN && person.getImageTop() < 768)
                person.setImageTop(person.getImageTop() + 2);
            if (person.getDirection() == Direction.UP && person.getImageTop() > 0)
                person.setImageTop(person.getImageTop() - 2);
        }

        return Integer.toString(person.getImageTop());
    }

    public String getLeftPosition() {
        if (person == null)
            return "0";

        if (person.isMoving()) {
            if (person.getDirection() == Direction.LEFT && person.getImageLeft() > 0)
                person.setImageLeft(person.getImageLeft() - 2);
            if (person.getDirection() == Direction.RIGHT && person.getImageLeft() < 1024)
                person.setImageLeft(person.getImageLeft() + 2);
        }

        return Integer.toString(person.getImageLeft());
    }

    public boolean isMoving() {
        if (person == null)
            return false;

        return person.isMoving();
    }

    public void setMoving(boolean moving) {
        if (person == null)
            return;

        person.setMoving(moving);
    }

    public void stop() {
        if (person == null)
            return;

        person.setMoving(false);
    }

    public void left() {
        person.setDirection(Direction.LEFT);
        person.setCurrentFrame(1);
        person.setMoving(true);
    }

    public void right() {
        person.setDirection(Direction.RIGHT);
        person.setCurrentFrame(1);
        person.setMoving(true);
    }

    public void up() {
        person.setDirection(Direction.UP);
        person.setCurrentFrame(1);
        person.setMoving(true);
    }

    public void down() {
        person.setDirection(Direction.DOWN);
        person.setCurrentFrame(1);
        person.setMoving(true);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
