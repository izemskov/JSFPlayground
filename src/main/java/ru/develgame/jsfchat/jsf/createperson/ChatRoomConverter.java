package ru.develgame.jsfchat.jsf.createperson;

import ru.develgame.jsfchat.db.entity.ChatRoom;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("chatRoomConverter")
public class ChatRoomConverter implements Converter<ChatRoom> {
    @Override
    public ChatRoom getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, ChatRoom chatRoom) {
        if (chatRoom != null) {
            return chatRoom.getName();
        }
        return null;
    }
}
