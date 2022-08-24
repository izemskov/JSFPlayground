package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.PersonsChangeInformer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
@ApplicationScoped
public class PersonsRegistry {
    @Inject
    private PersonsChangeInformer personsChangeInformer;

    private Map<String, Person> persons = new HashMap<>();

    public void addPerson(Person person) {
        if (person == null)
            return;

        persons.put(person.getUuid(), person);

        personsChangeInformer.sendMessage();
    }

    public void removePerson(Person person) {
        if (person == null)
            return;

        persons.remove(person.getUuid());

        personsChangeInformer.sendMessage();
    }

    public Map<String, Person> getPersons() {
        return persons;
    }
}
