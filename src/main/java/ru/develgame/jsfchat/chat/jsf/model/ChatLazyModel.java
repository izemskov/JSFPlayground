package ru.develgame.jsfchat.chat.jsf.model;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import ru.develgame.jsfchat.db.dao.ChatMessageDao;
import ru.develgame.jsfchat.db.entity.ChatMessage;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Dependent
public class ChatLazyModel extends LazyDataModel<ChatMessage> {
    @Inject
    private ChatMessageDao chatMessageDao;

    public static final int CHAT_PAGE_LIMIT = 6;

    @Override
    public int count(Map<String, FilterMeta> map) {
        return chatMessageDao.getAllMessagesCount();
    }

    @Override
    public List<ChatMessage> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        return chatMessageDao.getChatMessages(offset, CHAT_PAGE_LIMIT);
    }
}
