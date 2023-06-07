package ru.develgame.jsfgame.chat;

import ru.develgame.jsfgame.chat.entity.ChatMessage;
import ru.develgame.jsfgame.chat.model.ChatLazyModel;
import ru.develgame.jsfgame.user.UserBean;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;

import static ru.develgame.jsfgame.chat.model.ChatLazyModel.CHAT_PAGE_LIMIT;

@Named("chatRoom")
@ViewScoped
public class ChatRoomBean implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private ChatLazyModel chatLazyModel;

    @Inject
    private UserBean userBean;

    private String chatMessage;

    private int currentLoaded = CHAT_PAGE_LIMIT;

    public void addMessage() {
        if (chatMessage == null || chatMessage.isEmpty()) {
            return;
        }

        ChatMessage newMessage = new ChatMessage(userBean.getUsername(), chatMessage);
        try {
            userTransaction.begin();
            entityManager.persist(newMessage);
            userTransaction.commit();
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
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

    public int getCurrentLoaded() {
        return currentLoaded;
    }

    public void setCurrentLoaded(int currentLoaded) {
        this.currentLoaded = currentLoaded;
    }
}
