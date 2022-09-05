package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.ChatMessage;
import ru.develgame.jsfgame.domain.Direction;
import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.ChangesListener;
import ru.develgame.jsfgame.jms.MessagesType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("gameBean")
@SessionScoped
public class GameBean implements Serializable, MessageListener {
    @Inject
    private Person person;

    @EJB
    private PersonsRegistry personsRegistry;

    @Inject
    private ChangesListener changesListener;

    @EJB
    private ChatBean chatBean;

    @Inject
    private transient Logger logger;

    private List<Person> otherPersons;
    private List<ChatMessage> chatMessages;

    private boolean needUpdateOtherPersonsList = false;
    private boolean needUpdateChatMessages = false;

    private String chatMessage;

    @PostConstruct
    public void init() {
        otherPersons = readOtherPersonsList();
        chatMessages = readChatMessages();
        changesListener.addListener(this);
    }

    @PreDestroy
    public void finite() {
        changesListener.removeListener(this);
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
        try {
            if (((TextMessage) message).getText().equals(MessagesType.PERSON.toString()))
                needUpdateOtherPersonsList = true;
            else
                needUpdateChatMessages = true;
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Cannot get JMS message", e);
        }
    }

    private List<ChatMessage> readChatMessages() {
        return chatBean.getMessages();
    }

    public List<ChatMessage> getChatMessages() {
        if (needUpdateChatMessages) {
            chatMessages = readChatMessages();
            needUpdateChatMessages = false;
        }

        return chatMessages;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void sendChatMessage() {
        if (chatMessage != null && !chatMessage.isEmpty()) {
            chatBean.addMessage(person.getName(), chatMessage);
        }
    }
}
