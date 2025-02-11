package ru.develgame.jsfgame.common.jsf;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("menu")
@ApplicationScoped
public class MenuBean {
    public String game() {
        return "GAME";
    }

    public String chatRoom() {
        return "CHAT";
    }

    public String choosePerson() {
        return "CHOOSE_PERSON";
    }
}
