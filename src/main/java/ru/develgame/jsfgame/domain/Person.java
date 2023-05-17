package ru.develgame.jsfgame.domain;

import ru.develgame.jsfgame.PersonsRegistry;
import ru.develgame.jsfgame.utils.NameGenUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

@Dependent
public class Person implements Serializable {
    @EJB
    private PersonsRegistry personsRegistry;

    @EJB
    private NameGenUtil nameGenUtil;

    private String uuid = UUID.randomUUID().toString();

    protected int currentFrame = 1;

    protected int imageTop = 200;

    protected int imageLeft = new Random().nextInt(1024);

    protected Direction direction = Direction.DOWN;

    protected PersonType personType = PersonType.PERSON_TYPE1;

    protected Action action = Action.NONE;

    protected String name;

    private static final long serialVersionUID = -4004510546941410977L;

    @PostConstruct
    public void init() {
        personsRegistry.addPerson(this);
        name = nameGenUtil.getName();
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
        if (this.direction != direction) {
            setCurrentFrame(1);
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
                + currentAction.toString().toLowerCase() + "_"
                + direction.toString().toLowerCase()
                + currentFrame + ".png";
    }

    public int getHeight() {
        return personType.getHeight();
    }

    public int getWidth() {
        return personType.getWidth();
    }

    public void incrementCurrentFrame() {
        int maxFrame = personType.getMaxFrame();
        if (action == Action.ATTACK)
            maxFrame = personType.getMaxAttackFrame();

        currentFrame++;
        if (currentFrame > maxFrame)
            currentFrame = 1;
    }

    public void updateImage() {
        if (action != Action.NONE)
            incrementCurrentFrame();
    }

    public void updateTopPosition() {
        if (action == Action.WALK) {
            if (getDirection() == Direction.DOWN && (getImageTop() + getHeight()) < 768)
                setImageTop(getImageTop() + 2);
            if (getDirection() == Direction.UP && getImageTop() > 0)
                setImageTop(getImageTop() - 2);
        }
    }

    public void updateLeftPosition() {
        if (action == Action.WALK) {
            if (getDirection() == Direction.LEFT && getImageLeft() > 0)
                setImageLeft(getImageLeft() - 2);
            if (getDirection() == Direction.RIGHT && (getImageLeft() + getWidth()) < 1024)
                setImageLeft(getImageLeft() + 2);
        }
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if ((this.action == Action.WALK && action == Action.ATTACK) || (this.action == Action.ATTACK && action == Action.WALK))
            setCurrentFrame(1);
        this.action = action;
    }
}
