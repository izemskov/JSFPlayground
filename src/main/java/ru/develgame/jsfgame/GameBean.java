package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.ChatMessage;
import ru.develgame.jsfgame.domain.Direction;
import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.ChangesListener;
import ru.develgame.jsfgame.jms.MessagesType;
import ru.develgame.jsfgame.player.PlayerBean;

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
    private PlayerBean playerBean;

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
        playerBean.getPerson().updateImage();
        playerBean.getPerson().updateTopPosition();
        playerBean.getPerson().updateLeftPosition();
    }

    public List<Person> getOtherPersons() {
        if (needUpdateOtherPersonsList) {
            otherPersons = readOtherPersonsList();
            needUpdateOtherPersonsList = false;
        }

        return otherPersons;
    }

    public void stop() {
        playerBean.getPerson().setMoving(false);
    }

    public void left() {
        playerBean.getPerson().setDirection(Direction.LEFT);
        playerBean.getPerson().setMoving(true);
    }

    public void right() {
        playerBean.getPerson().setDirection(Direction.RIGHT);
        playerBean.getPerson().setMoving(true);
    }

    public void up() {
        playerBean.getPerson().setDirection(Direction.UP);
        playerBean.getPerson().setMoving(true);
    }

    public void down() {
        playerBean.getPerson().setDirection(Direction.DOWN);
        playerBean.getPerson().setMoving(true);
    }

    public Person getPerson() {
        return playerBean.getPerson();
    }

    private List<Person> readOtherPersonsList() {
        return personsRegistry.getOtherPersonsList(playerBean.getPerson().getUuid());
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
            chatBean.addMessage(playerBean.getPerson().getName(), chatMessage);
        }
    }
}
