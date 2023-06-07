package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Action;
import ru.develgame.jsfgame.domain.Direction;
import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.ChangesListener;
import ru.develgame.jsfgame.jms.MessagesType;
import ru.develgame.jsfgame.user.UserBean;

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
    private UserBean userBean;

    @EJB
    private transient PersonsRegistry personsRegistry;

    @Inject
    private transient ChangesListener changesListener;

    @Inject
    private transient Logger logger;

    private List<Person> otherPersons;

    private boolean needUpdateOtherPersonsList = false;
    private boolean needUpdateChatMessages = false;

    private String chatMessage;

    private static final long serialVersionUID = -347847984647877844L;

    @PostConstruct
    public void init() {
        otherPersons = readOtherPersonsList();
        changesListener.addListener(this);
    }

    @PreDestroy
    public void finite() {
        changesListener.removeListener(this);
    }

    public void updatePerson() {
        userBean.getPerson().updateImage();
        userBean.getPerson().updateTopPosition();
        userBean.getPerson().updateLeftPosition();
    }

    public List<Person> getOtherPersons() {
        if (needUpdateOtherPersonsList) {
            otherPersons = readOtherPersonsList();
            needUpdateOtherPersonsList = false;
        }

        return otherPersons;
    }

    public void stop() {
        userBean.getPerson().setAction(Action.NONE);
    }

    public void left() {
        userBean.getPerson().setDirection(Direction.LEFT);
        userBean.getPerson().setAction(Action.WALK);
    }

    public void right() {
        userBean.getPerson().setDirection(Direction.RIGHT);
        userBean.getPerson().setAction(Action.WALK);
    }

    public void up() {
        userBean.getPerson().setDirection(Direction.UP);
        userBean.getPerson().setAction(Action.WALK);
    }

    public void down() {
        userBean.getPerson().setDirection(Direction.DOWN);
        userBean.getPerson().setAction(Action.WALK);
    }

    public Person getPerson() {
        return userBean.getPerson();
    }

    private List<Person> readOtherPersonsList() {
        return personsRegistry.getOtherPersonsList(userBean.getPerson().getUuid());
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

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
