package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.ChatMessage
import ru.develgame.jsfgame.domain.Person
import javax.ejb.*

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
open class ChatBean {
    private val messages = mutableListOf<ChatMessage>()

    @Lock(LockType.WRITE)
    open fun addMessage(header: String, message: String) {
        if (header.length > MAX_HEADER_SIZE)
            header.substring(header.length - MAX_HEADER_SIZE)

        messages.add(ChatMessage(header, message))

        if (messages.size > MAX_MESSAGES_COUNT) {
            messages.removeAt(0)
        }
    }

    @Lock(LockType.READ)
    open fun getMessages(): List<ChatMessage> = messages.toList()

    open fun personJoined(person: Person) {
        addMessage(person.uuid, "Person ${person.uuid} joined")
    }

    companion object {
        const val MAX_MESSAGES_COUNT = 10
        const val MAX_HEADER_SIZE = 15
    }
}