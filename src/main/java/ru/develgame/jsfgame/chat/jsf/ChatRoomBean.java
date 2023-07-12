package ru.develgame.jsfgame.chat.jsf;

import ru.develgame.jsfgame.chat.entity.ChatMessage;
import ru.develgame.jsfgame.chat.jsf.model.ChatLazyModel;
import ru.develgame.jsfgame.jms.ChangesInformer;
import ru.develgame.jsfgame.jms.ChangesListener;
import ru.develgame.jsfgame.jms.MessagesType;
import ru.develgame.jsfgame.user.UserBean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.develgame.jsfgame.chat.jsf.model.ChatLazyModel.CHAT_PAGE_LIMIT;

@Named("chatRoom")
@SessionScoped
public class ChatRoomBean implements Serializable, MessageListener {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private ChatLazyModel chatLazyModel;

    @Inject
    private UserBean userBean;

    @Inject
    private ChangesInformer changesInformer;

    @Inject
    private transient ChangesListener changesListener;

    @Inject
    private transient Logger logger;

    @Inject
    @Push(channel = "chatRoomChannel")
    private PushContext chatRoomChannel;

    public void sendMessage(Object message) {
        chatRoomChannel.send(message);
    }

    private String chatMessage;

    private int chatPageLimit = CHAT_PAGE_LIMIT;

    @PostConstruct
    public void init() {
        changesListener.addListener(this);
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

    public boolean isOwnMessage(String username) {
        return userBean.getUsername().equals(username);
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatLazyModel getChatLazyModel() {
        return chatLazyModel;
    }

    public int getChatPageLimit() {
        return chatPageLimit;
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (((TextMessage) message).getText().equals(MessagesType.CHAT.toString())) {
                sendMessage("update");
            }
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Cannot get JMS message", e);
        }
    }
}
