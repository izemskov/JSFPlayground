package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
@ApplicationScoped
public class PersonsRegistry {
    private Map<String, Person> persons = new HashMap<>();

    public void addPerson(Person person) {
        if (person == null)
            return;

        persons.put(person.getUuid(), person);
    }

    public void removePerson(Person person) {
        if (person == null)
            return;

        persons.remove(person.getUuid());
    }

    public Map<String, Person> getPersons() {
        return persons;
    }
}
