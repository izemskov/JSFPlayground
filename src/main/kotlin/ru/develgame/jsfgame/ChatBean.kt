package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.ChatMessage
import ru.develgame.jsfgame.domain.Person
import ru.develgame.jsfgame.jms.ChangesInformer
import ru.develgame.jsfgame.jms.MessagesType
import javax.ejb.*
import javax.inject.Inject

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
open class ChatBean {
    @Inject
    private lateinit var changesInformer: ChangesInformer

    private val messages = mutableListOf<ChatMessage>()

    @Lock(LockType.WRITE)
    open fun addMessage(header: String, message: String) {
        if (header.length > MAX_HEADER_SIZE)
            header.substring(header.length - MAX_HEADER_SIZE)

        messages.add(ChatMessage(header, message))

        if (messages.size > MAX_MESSAGES_COUNT) {
            messages.removeAt(0)
        }

        changesInformer.sendMessage(MessagesType.CHAT)
    }

    @Lock(LockType.READ)
    open fun getMessages(): List<ChatMessage> = messages.toList()

    open fun personJoined(person: Person) {
        addMessage(person.name, "Person ${person.name} joined")
    }

    companion object {
        const val MAX_MESSAGES_COUNT = 10
        const val MAX_HEADER_SIZE = 15
    }
}