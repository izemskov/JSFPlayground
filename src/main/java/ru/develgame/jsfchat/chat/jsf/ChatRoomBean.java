package ru.develgame.jsfchat.chat.jsf;

import ru.develgame.jsfchat.db.dao.ChatMessageDao;
import ru.develgame.jsfchat.db.entity.ChatMessage;
import ru.develgame.jsfchat.chat.jsf.model.ChatLazyModel;
import ru.develgame.jsfchat.jms.ChangesInformer;
import ru.develgame.jsfchat.jms.ChangesListener;
import ru.develgame.jsfchat.jms.MessagesType;
import ru.develgame.jsfchat.user.UserBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.develgame.jsfchat.chat.jsf.model.ChatLazyModel.CHAT_PAGE_LIMIT;

@Named("chatRoom")
@SessionScoped
public class ChatRoomBean implements Serializable, MessageListener {
    @Inject
    private transient ChatMessageDao chatMessageDao;

    @Inject
    private ChatLazyModel chatLazyModel;

    @Inject
    private UserBean userBean;

    @Inject
    private transient ChangesInformer changesInformer;

    @Inject
    private transient ChangesListener changesListener;

    @Inject
    private transient Logger logger;

    @Inject
    @Push(channel = "chatRoomChannel")
    private PushContext chatRoomChannel;

    private String chatMessage;

    private int chatPageLimit = CHAT_PAGE_LIMIT;

    @PostConstruct
    public void init() {
        changesListener.addListener(this);
    }

    @PreDestroy
    public void finite() {
        changesListener.removeListener(this);
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
                chatRoomChannel.send("update");
            }
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Cannot get JMS message", e);
        }
    }

    private static final long serialVersionUID = -4280928171896927886L;
}
