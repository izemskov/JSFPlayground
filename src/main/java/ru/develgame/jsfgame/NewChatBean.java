package ru.develgame.jsfgame;

import ru.develgame.jsfgame.entity.ChatMessage;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@Named("newChatBean")
@RequestScoped
public class NewChatBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public void addMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser(), "Testsetest");
        chatMessage.setId(1000L);
        try {
            userTransaction.begin();
            entityManager.persist(chatMessage);
            userTransaction.commit();
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }
}
