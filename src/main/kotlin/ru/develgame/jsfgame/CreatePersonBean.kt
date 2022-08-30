package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.Person
import java.io.Serializable
import javax.enterprise.context.SessionScoped
import javax.inject.Inject
import javax.inject.Named

@Named("createPerson")
@SessionScoped
open class CreatePersonBean : Serializable {
    @Inject
    private lateinit var person: Person

    open fun getPerson() = person

    open fun createPerson(): String {
        if (person.name.isNullOrEmpty())
            person.name = person.uuid

        return "GAME"
    }
}