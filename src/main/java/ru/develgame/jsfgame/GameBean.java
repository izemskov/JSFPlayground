package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.ChatMessage;
import ru.develgame.jsfgame.domain.Direction;
import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.PersonsChangeListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.Serializable;
import java.util.*;

@Named("gameBean")
@SessionScoped
public class GameBean implements Serializable, MessageListener {
    @Inject
    private Person person;

    @EJB
    private PersonsRegistry personsRegistry;

    @Inject
    private PersonsChangeListener personsChangeListener;

    @Inject
    private ChatBean chatBean;

    private List<Person> otherPersons;

    private boolean needUpdateOtherPersonsList = false;

    @PostConstruct
    public void init() {
        otherPersons = readOtherPersonsList();
        personsChangeListener.addListener(this);
    }

    @PreDestroy
    public void finite() {
        personsChangeListener.removeListener(this);
    }

    public void updatePerson() {
        updateImage();
        updateTopPosition();
        updateLeftPosition();
    }

    private void updateImage() {
        if (person.isMoving())
            person.incrementCurrentFrame();
    }

    private void updateTopPosition() {
        if (person.isMoving()) {
            if (person.getDirection() == Direction.DOWN && (person.getImageTop() + person.getHeight()) < 768)
                person.setImageTop(person.getImageTop() + 2);
            if (person.getDirection() == Direction.UP && person.getImageTop() > 0)
                person.setImageTop(person.getImageTop() - 2);
        }
    }

    private void updateLeftPosition() {
        if (person.isMoving()) {
            if (person.getDirection() == Direction.LEFT && person.getImageLeft() > 0)
                person.setImageLeft(person.getImageLeft() - 2);
            if (person.getDirection() == Direction.RIGHT && (person.getImageLeft() + person.getWidth()) < 1024)
                person.setImageLeft(person.getImageLeft() + 2);
        }
    }

    public List<Person> getOtherPersons() {
        if (needUpdateOtherPersonsList) {
            otherPersons = readOtherPersonsList();
            needUpdateOtherPersonsList = false;
        }

        return otherPersons;
    }

    public void stop() {
        person.setMoving(false);
    }

    public void left() {
        person.setDirection(Direction.LEFT);
        person.setMoving(true);
    }

    public void right() {
        person.setDirection(Direction.RIGHT);
        person.setMoving(true);
    }

    public void up() {
        person.setDirection(Direction.UP);
        person.setMoving(true);
    }

    public void down() {
        person.setDirection(Direction.DOWN);
        person.setMoving(true);
    }

    public Person getPerson() {
        return person;
    }

    private List<Person> readOtherPersonsList() {
        return personsRegistry.getOtherPersonsList(person.getUuid());
    }

    @Override
    public void onMessage(Message message) {
        needUpdateOtherPersonsList = true;
    }

    public List<ChatMessage> getChatMessages() {
        return new LinkedList<>(chatBean.getMessages());
    }
}
