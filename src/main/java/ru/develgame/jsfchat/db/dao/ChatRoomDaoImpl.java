package ru.develgame.jsfchat.db.dao;

import ru.develgame.jsfchat.db.entity.ChatRoom;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class ChatRoomDaoImpl implements ChatRoomDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatRoom> getAllRooms() {
        Query query = entityManager.createNativeQuery(
                "SELECT ID, NAME, PWD FROM APP.CHAT_ROOM ORDER BY NAME ASC",
                ChatRoom.class);
        List messages = new ArrayList<>(query.getResultList());
        return messages;
    }
}
