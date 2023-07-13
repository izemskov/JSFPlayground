package ru.develgame.jsfgame.chat.dao;

import ru.develgame.jsfgame.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageDao {
    List<ChatMessage> getChatMessages(int offset, int pageSize);

    ChatMessage getLastMessage();

    Integer getAllMessagesCount();

    boolean addChatMessage(ChatMessage chatMessage);
}
