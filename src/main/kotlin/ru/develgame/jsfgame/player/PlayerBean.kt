package ru.develgame.jsfgame.player

import ru.develgame.jsfgame.domain.Person
import java.io.Serializable
import javax.enterprise.context.SessionScoped
import javax.inject.Inject

@SessionScoped
open class PlayerBean: Serializable {
    @Inject
    private lateinit var person: Person

    open fun getPerson() = person
}