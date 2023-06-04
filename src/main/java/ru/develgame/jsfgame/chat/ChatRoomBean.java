package ru.develgame.jsfgame.chat;

import ru.develgame.jsfgame.entity.ChatMessage;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;

@Named("chatRoom")
@SessionScoped
public class ChatRoomBean implements Serializable {
    private static final int CHAT_PAGE_LIMIT = 6;

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private ChatLazyModel chatLazyModel;

    private String chatMessage;

    public void addMessage() {
        if (chatMessage == null || chatMessage.isEmpty()) {
            return;
        }

        ChatMessage newMessage = new ChatMessage(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser(), chatMessage);
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

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatLazyModel getChatLazyModel() {
        return chatLazyModel;
    }
}
