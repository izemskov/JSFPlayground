package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.jms.ChangesInformer;
import ru.develgame.jsfgame.jms.MessagesType;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class PersonsRegistry {
    @Inject
    private ChangesInformer changesInformer;

    private final Map<String, Person> persons = new HashMap<>();

    @Lock(WRITE)
    public void addPerson(Person person) {
        persons.put(person.getUuid(), person);
        changesInformer.sendMessage(MessagesType.PERSON);
    }

    @Lock(WRITE)
    public void removePerson(Person person) {
        persons.remove(person.getUuid());
        changesInformer.sendMessage(MessagesType.PERSON);
    }

    @Lock(READ)
    public List<Person> getOtherPersonsList(String uuid) {
        return persons.values().stream().filter(t -> !t.getUuid().equals(uuid)).collect(Collectors.toList());
    }
}
