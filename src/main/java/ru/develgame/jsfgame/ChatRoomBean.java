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
import java.util.Collection;
import java.util.List;

@Named("chatRoom")
@SessionScoped
public class ChatRoomBean implements Serializable {
    private static final int CHAT_PAGE_LIMIT = 2;

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public List<ChatMessage> getMessages(int offset) {
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET " + offset + " ROWS FETCH NEXT " + CHAT_PAGE_LIMIT + " ROWS ONLY",
                ChatMessage.class);
        return new ArrayList<>(query.getResultList());
    }

    public void addMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser(), "Testsetest");
        try {
            userTransaction.begin();
            entityManager.persist(chatMessage);
            userTransaction.commit();
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }

        Query query = entityManager.createQuery("SELECT e FROM ChatMessage e");
        Collection<ChatMessage> resultList = query.getResultList();
    }
}
