package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Direction;
import ru.develgame.jsfgame.domain.Person;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named("gameBean")
@SessionScoped
public class GameBean implements Serializable {
    @Inject
    private Person person;

    @Inject
    private PersonsRegistry personsRegistry;

    private List<Person> otherPersons = new ArrayList<>();

    public void updatePerson() {
        updateImage();
        updateTopPosition();
        updateLeftPosition();
    }

    private void updateImage() {
        if (person.isMoving())
            person.setCurrentFrame(person.getCurrentFrame() + 1);
        if (person.getCurrentFrame() > 8)
            person.setCurrentFrame(1);
    }

    private void updateTopPosition() {
        if (person.isMoving()) {
            if (person.getDirection() == Direction.DOWN && person.getImageTop() < 768)
                person.setImageTop(person.getImageTop() + 2);
            if (person.getDirection() == Direction.UP && person.getImageTop() > 0)
                person.setImageTop(person.getImageTop() - 2);
        }
    }

    private void updateLeftPosition() {
        if (person.isMoving()) {
            if (person.getDirection() == Direction.LEFT && person.getImageLeft() > 0)
                person.setImageLeft(person.getImageLeft() - 2);
            if (person.getDirection() == Direction.RIGHT && person.getImageLeft() < 1024)
                person.setImageLeft(person.getImageLeft() + 2);
        }
    }

    public List<Person> getOtherPersons() {
        // TODO - optimize that
        otherPersons.clear();

        Map<String, Person> persons = personsRegistry.getPersons();
        for (Map.Entry<String, Person> entry : persons.entrySet()) {
            if (!entry.getKey().equals(person.getUuid())) {
                otherPersons.add(entry.getValue());
            }
        }

        return otherPersons;
    }

    public void stop() {
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
}
