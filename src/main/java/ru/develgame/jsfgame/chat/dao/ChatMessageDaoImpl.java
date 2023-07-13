package ru.develgame.jsfgame.chat.dao;

import ru.develgame.jsfgame.chat.entity.ChatMessage;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class ChatMessageDaoImpl implements ChatMessageDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private transient Logger logger;

    @Override
    public List<ChatMessage> getChatMessages(int offset, int pageSize) {
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET " + offset + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY",
                ChatMessage.class);
        List messages = new ArrayList<>(query.getResultList());
        return messages;
    }

    @Override
    public ChatMessage getLastMessage() {
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY",
                ChatMessage.class);
        return (ChatMessage) query.getSingleResult();
    }

    @Override
    public Integer getAllMessagesCount() {
        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM APP.CHAT");
        return (Integer) query.getSingleResult();
    }

    @Override
    public boolean addChatMessage(ChatMessage chatMessage) {
        try {
            userTransaction.begin();
            entityManager.persist(chatMessage);
            userTransaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot add chat message", e);
            return false;
        }

        return true;
    }
}
