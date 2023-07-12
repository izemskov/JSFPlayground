package ru.develgame.jsfgame.game.jsf;

import ru.develgame.jsfgame.chat.entity.ChatMessage;
import ru.develgame.jsfgame.game.PersonsRegistry;
import ru.develgame.jsfgame.game.domain.Action;
import ru.develgame.jsfgame.game.domain.Direction;
import ru.develgame.jsfgame.game.domain.Person;
import ru.develgame.jsfgame.jms.ChangesInformer;
import ru.develgame.jsfgame.jms.ChangesListener;
import ru.develgame.jsfgame.jms.MessagesType;
import ru.develgame.jsfgame.user.UserBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    @Push(channel = "chatChannel")
    private PushContext chatChannel;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private ChangesInformer changesInformer;

    private List<Person> otherPersons;

    private List<ChatMessage> chatMessages;

    private Map<String, String> personLastMessage = new HashMap<>();

    private boolean needUpdateOtherPersonsList = false;

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
                Query query = entityManager.createNativeQuery(
                        "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY",
                        ChatMessage.class);
                ChatMessage lastMessage = (ChatMessage) query.getSingleResult();
                if (lastMessage != null) {
                    personLastMessage.put(lastMessage.getName(), lastMessage.getMessage());
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
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY",
                ChatMessage.class);
        List messages = new ArrayList<>(query.getResultList());
        return messages;
    }

    public String getPersonLastMessage(String name) {
        return personLastMessage.get(name);
    }

    public void addMessage() {
        if (chatMessage == null || chatMessage.isEmpty()) {
            return;
        }

        ChatMessage newMessage = new ChatMessage(userBean.getUsername(), chatMessage);
        try {
            userTransaction.begin();
            entityManager.persist(newMessage);
            userTransaction.commit();

            changesInformer.sendMessage(MessagesType.CHAT);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot add chat message", e);
        }

        chatMessage = "";
    }
}
