package ru.develgame.jsfgame.user.jsf;

import ru.develgame.jsfgame.chat.entity.ChatRoom;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("createPerson")
@ViewScoped
public class CreatePersonBean implements Serializable {
    private List<ChatRoom> chatRooms = new ArrayList<>();

    private ChatRoom chatRoom;

    @PostConstruct
    public void init() {
        chatRooms.add(new ChatRoom("test"));
        chatRooms.add(new ChatRoom("test1"));
        chatRooms.add(new ChatRoom("test2"));
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
