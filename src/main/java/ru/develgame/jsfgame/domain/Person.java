package ru.develgame.jsfgame.domain;

import ru.develgame.jsfgame.PersonsRegistry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.UUID;

@SessionScoped
public class Person implements Serializable {
    @Inject
    private PersonsRegistry personsRegistry;

    private String uuid = UUID.randomUUID().toString();

    private int currentFrame = 1;

    private int imageTop = 200;

    private int imageLeft = 200;

    private Direction direction = Direction.DOWN;

    private PersonType personType = PersonType.PERSON_TYPE1;

    private boolean moving = false;

    private String name;

    @PostConstruct
    public void init() {
        personsRegistry.addPerson(this);
        name = uuid;
    }

    @PreDestroy
    public void finite() {
        personsRegistry.removePerson(this);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getImageTop() {
        return imageTop;
    }

    public void setImageTop(int imageTop) {
        this.imageTop = imageTop;
    }

    public int getImageLeft() {
        return imageLeft;
    }

    public void setImageLeft(int imageLeft) {
        this.imageLeft = imageLeft;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopPosition() {
        return Integer.toString(imageTop);
    }

    public String getLeftPosition() {
        return Integer.toString(imageLeft);
    }

    public String getImage() {
        return "/images/" + personType.toString() + "/walk_" + direction.toString().toLowerCase()
                + currentFrame + ".png";
    }
}
