package ru.develgame.jsfchat.game;

import ru.develgame.jsfchat.game.domain.Person;
import ru.develgame.jsfchat.jms.ChangesInformer;
import ru.develgame.jsfchat.jms.MessagesType;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
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

    @Resource
    private TimerService timerService;

    private final Map<String, Person> persons = new HashMap<>();

    @PostConstruct
    public void initialize() {
        timerService.createTimer(1000, 100, "Interval");
    }

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

    @Timeout
    @Lock(READ)
    public void updatePersonTimeout(Timer timer) {
        for (Person person : persons.values()) {
            person.updatePerson();
        }
    }
}
