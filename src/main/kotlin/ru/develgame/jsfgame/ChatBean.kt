package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.ChatMessage
import ru.develgame.jsfgame.domain.Person
import java.util.concurrent.ConcurrentLinkedQueue
import javax.enterprise.context.ApplicationScoped
import javax.inject.Named

@Named
@ApplicationScoped
open class ChatBean {
    open val messages = ConcurrentLinkedQueue<ChatMessage>()

    open fun personJoined(person: Person) {
        messages.add(ChatMessage(person.uuid.substring(person.uuid.length - 8),
                "Person ${person.uuid} joined"))
    }
}