package ru.develgame.jsfgame.chat;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import ru.develgame.jsfgame.entity.ChatMessage;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dependent
public class ChatLazyModel extends LazyDataModel<ChatMessage> {
    @PersistenceContext
    private EntityManager entityManager;

    private static final int CHAT_PAGE_LIMIT = 6;

    @Override
    public int count(Map<String, FilterMeta> map) {
        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM APP.CHAT");
        return (Integer) query.getSingleResult();
    }

    @Override
    public List<ChatMessage> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, MESSAGE, CREATEDAT FROM APP.CHAT ORDER BY CREATEDAT DESC OFFSET " + offset + " ROWS FETCH NEXT " + CHAT_PAGE_LIMIT + " ROWS ONLY",
                ChatMessage.class);
        List messages = new ArrayList<>(query.getResultList());
        return messages;
    }
}
