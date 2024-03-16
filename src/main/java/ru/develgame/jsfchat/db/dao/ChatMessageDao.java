package ru.develgame.jsfchat.db.dao;

import ru.develgame.jsfchat.db.entity.ChatMessage;

import java.util.List;

public interface ChatMessageDao {
    List<ChatMessage> getChatMessages(int offset, int pageSize);

    ChatMessage getLastMessage();

    Integer getAllMessagesCount();

    boolean addChatMessage(ChatMessage chatMessage);
}
