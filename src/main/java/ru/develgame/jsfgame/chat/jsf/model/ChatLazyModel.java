package ru.develgame.jsfgame.chat.jsf.model;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import ru.develgame.jsfgame.chat.dao.ChatMessageDao;
import ru.develgame.jsfgame.chat.entity.ChatMessage;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
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
