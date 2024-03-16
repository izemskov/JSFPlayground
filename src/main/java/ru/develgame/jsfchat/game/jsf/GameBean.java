package ru.develgame.jsfchat.game.jsf;

import ru.develgame.jsfchat.db.dao.ChatMessageDao;
import ru.develgame.jsfchat.db.entity.ChatMessage;
import ru.develgame.jsfchat.game.PersonsRegistry;
import ru.develgame.jsfchat.game.domain.Action;
import ru.develgame.jsfchat.game.domain.Direction;
import ru.develgame.jsfchat.game.domain.Person;
import ru.develgame.jsfchat.jms.ChangesInformer;
import ru.develgame.jsfchat.jms.ChangesListener;
import ru.develgame.jsfchat.jms.MessagesType;
import ru.develgame.jsfchat.user.UserBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
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

    @Inject
    private transient ChangesInformer changesInformer;

    @Inject
    private transient ChatMessageDao chatMessageDao;

    @Inject
    @Push(channel = "chatChannel")
    private PushContext chatChannel;

    private List<Person> otherPersons;

    private Map<String, String> personLastMessage = new HashMap<>();

    private boolean needUpdateOtherPersonsList = false;

    private String chatMessage;

    @PostConstruct
    public void init() {
        otherPersons = readOtherPersonsList();
        changesListener.addListener(this);
    }

    @PreDestroy
    public void finite() {
        changesListener.removeListener(this);
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
            if (((TextMessage) message).getText().equals(MessagesType.PERSON.toString())) {
                needUpdateOtherPersonsList = true;
            }
            else if (((TextMessage) message).getText().equals(MessagesType.CHAT.toString())) {
                chatChannel.send("update");

                ChatMessage lastMessage = chatMessageDao.getLastMessage();
                if (lastMessage != null) {
                    String text = lastMessage.getMessage();
                    if (text.length() > 30) {
                        text = text.substring(0, 27) + "...";
                    }
                    personLastMessage.put(lastMessage.getName(), text);
                }
            }
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

    public List<ChatMessage> getChatMessages() {
        return chatMessageDao.getChatMessages(0, 10);
    }

    public String getPersonLastMessage(String name) {
        return personLastMessage.get(name);
    }

    public void addMessage() {
        if (chatMessage == null || chatMessage.isEmpty()) {
            return;
        }

        // TODO
        if (chatMessageDao.addChatMessage(new ChatMessage(userBean.getUsername(), chatMessage, null))) {
            changesInformer.sendMessage(MessagesType.CHAT);
        }

        chatMessage = "";
    }

    private static final long serialVersionUID = -347847984647877844L;
}
