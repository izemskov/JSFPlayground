package ru.develgame.jsfgame;

import ru.develgame.jsfgame.entity.ChatMessage;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.Collection;

@Named("newChatBean")
@RequestScoped
public class NewChatBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

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
