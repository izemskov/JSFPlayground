package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.Person
import ru.develgame.jsfgame.jms.PersonsChangeInformer
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.inject.Named

@Named
@ApplicationScoped
open class PersonsRegistry {
    @Inject
    private lateinit var personsChangeInformer: PersonsChangeInformer

    open val persons: MutableMap<String, Person> = mutableMapOf()

    open fun addPerson(person: Person) {
        persons[person.uuid] = person

        personsChangeInformer.sendMessage()
    }

    open fun removePerson(person: Person) {
        persons.remove(person.uuid)

        personsChangeInformer.sendMessage()
    }
}