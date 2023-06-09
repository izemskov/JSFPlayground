package ru.develgame.jsfgame.game.jsf;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("createPerson")
@SessionScoped
public class CreatePersonBean implements Serializable {
    private static final long serialVersionUID = 9138108733190235780L;

    public String game() {
        return "GAME";
    }

    public String chatRoom() {
        return "CHAT";
    }
}
