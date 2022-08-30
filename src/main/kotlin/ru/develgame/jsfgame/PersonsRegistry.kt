package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.Direction
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

    open fun checkCollision(person: Person): Boolean {
        if (person.isMoving) {
            for ((key, value) in persons) {
                if (key != person.uuid) {
                    if (person.direction == Direction.DOWN) {
                        if (person.imageTop + person.height > value.imageTop)
                            return true
                    } else if (person.direction == Direction.UP) {
                        if (person.imageTop < value.imageTop + value.height)
                            return true
                    }
                    else if (person.direction == Direction.LEFT) {
                        if (person.imageLeft < value.imageLeft + value.width)
                            return true
                    }
                    else if (person.direction == Direction.RIGHT) {
                        if (person.imageLeft + person.width > value.imageLeft)
                            return true
                    }
                }
            }
        }

        return false
    }
}