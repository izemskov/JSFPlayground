package ru.develgame.jsfgame.game.domain;

import ru.develgame.jsfgame.game.PersonsRegistry;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import java.io.Serializable;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Dependent
public class Person implements Serializable {
    @EJB
    private transient PersonsRegistry personsRegistry;

    private String uuid = UUID.randomUUID().toString();

    protected AtomicInteger currentFrame = new AtomicInteger(1);

    protected int imageTop = 200;

    protected int imageLeft = new Random().nextInt(1024);

    protected Direction direction = Direction.DOWN;

    protected PersonType personType = PersonType.PERSON_TYPE1;

    protected Action action = Action.NONE;

    protected String name;

    @PostConstruct
    public void init() {
        personsRegistry.addPerson(this);
    }

    @PreDestroy
    public void finite() {
        personsRegistry.removePerson(this);
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
        if (this.direction != direction) {
            currentFrame.set(1);
        }

        this.direction = direction;
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
        Action currentAction = action;
        if (currentAction == Action.NONE)
            currentAction = Action.WALK;

        return "/images/" + personType.toString() + "/"
                + currentAction.toString().toLowerCase(Locale.US) + "_"
                + direction.toString().toLowerCase(Locale.US)
                + currentFrame.get() + ".png";
    }

    public int getHeight() {
        return personType.getHeight();
    }

    public int getWidth() {
        return personType.getWidth();
    }

    private void incrementCurrentFrame() {
        int maxFrame = personType.getMaxFrame();

        int currentFrameVal = currentFrame.get() + 1;

        if (currentFrameVal > maxFrame) {
            currentFrameVal = 1;
        }

        currentFrame.set(currentFrameVal);
    }

    private void updateImage() {
        if (action != Action.NONE)
            incrementCurrentFrame();
    }

    private void updateTopPosition() {
        if (action == Action.WALK) {
            if (getDirection() == Direction.DOWN && (getImageTop() + getHeight()) < 768)
                setImageTop(getImageTop() + 2);
            if (getDirection() == Direction.UP && getImageTop() > 0)
                setImageTop(getImageTop() - 2);
        }
    }

    private void updateLeftPosition() {
        if (action == Action.WALK) {
            if (getDirection() == Direction.LEFT && getImageLeft() > 0)
                setImageLeft(getImageLeft() - 2);
            if (getDirection() == Direction.RIGHT && (getImageLeft() + getWidth()) < 1024)
                setImageLeft(getImageLeft() + 2);
        }
    }

    public void updatePerson() {
        updateImage();
        updateTopPosition();
        updateLeftPosition();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    private static final long serialVersionUID = -4004510546941410977L;
}
