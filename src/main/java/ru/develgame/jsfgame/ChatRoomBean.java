package ru.develgame.jsfgame;

import ru.develgame.jsfgame.entity.ChatMessage;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named("chatRoom")
@SessionScoped
public class ChatRoomBean implements Serializable {
    private static final int CHAT_PAGE_LIMIT = 6;

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    private String chatMessage;

    public List<ChatMessage> getMessages(int offset) {
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET " + offset + " ROWS FETCH NEXT " + CHAT_PAGE_LIMIT + " ROWS ONLY",
                ChatMessage.class);
        List messages = new ArrayList<>(query.getResultList());
        Collections.reverse(messages);
        return messages;
    }

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
}
