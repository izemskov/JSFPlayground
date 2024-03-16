package ru.develgame.jsfchat.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CHAT", schema = "APP")
public class ChatMessage implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "CREATEDAT")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name="CHAT_ROOM_ID")
    private ChatRoom chatRoomId;

    public ChatMessage() {
        createdAt = new Date();
    }

    public ChatMessage(String name, String message, ChatRoom chatRoomId) {
        this.name = name;
        this.message = message;
        this.chatRoomId = chatRoomId;
        createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ChatRoom getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(ChatRoom chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    private static final long serialVersionUID = -1186283662169435291L;
}
