package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Person;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("createPerson")
@SessionScoped
public class CreatePersonBean implements Serializable {
    @Inject
    private Person person;

    public Person getPerson() {
        return person;
    }

    public String createPerson() {
        if (person.getName() == null || person.getName().isEmpty())
            person.setName(person.getUuid());

        return "GAME";
    }
}
