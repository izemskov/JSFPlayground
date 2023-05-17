package ru.develgame.jsfgame.player;

import ru.develgame.jsfgame.domain.Person;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class PlayerBean implements Serializable {
    @Inject
    private Person person;

    private static final long serialVersionUID = -4473885537493656995L;

    public Person getPerson() {
        return person;
    }
}
