package ru.develgame.jsfchat.db.dao;

import ru.develgame.jsfchat.db.entity.ChatRoom;

import java.util.List;

public interface ChatRoomDao {
    List<ChatRoom> getAllRooms();
}
