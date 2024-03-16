package ru.develgame.jsfchat.common.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

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
