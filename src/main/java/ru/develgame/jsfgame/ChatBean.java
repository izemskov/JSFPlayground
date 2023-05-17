package ru.develgame.jsfgame;

import com.sun.xml.internal.ws.util.StringUtils;
import ru.develgame.jsfgame.domain.ChatMessage;
import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.ChangesInformer;
import ru.develgame.jsfgame.jms.MessagesType;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ChatBean {
    @Inject
    private ChangesInformer changesInformer;

    private final List<ChatMessage> messages = new ArrayList<>();

    private static final int MAX_MESSAGES_COUNT = 10;

    private static final int MAX_HEADER_SIZE = 15;

    @Lock(LockType.WRITE)
    public void addMessage(String header, String message) {
        if (header.length() > MAX_HEADER_SIZE) {
            header = header.substring(header.length() - MAX_HEADER_SIZE);
        }

        messages.add(new ChatMessage(header, message));

        if (messages.size() > MAX_MESSAGES_COUNT) {
            messages.remove(0);
        }

        changesInformer.sendMessage(MessagesType.CHAT);
    }

    @Lock(LockType.READ)
    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public void personJoined(Person person) {
        addMessage(person.getName(), String.format("Person %s joined", person.getName()));
    }
}
