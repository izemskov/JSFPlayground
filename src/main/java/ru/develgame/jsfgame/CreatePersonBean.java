package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.user.UserBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("createPerson")
@SessionScoped
public class CreatePersonBean implements Serializable {
    @Inject
    private UserBean userBean;

    private static final long serialVersionUID = 9138108733190235780L;

    public Person getPerson() {
        return userBean.getPerson();
    }

    public String game() {
        return "GAME";
    }

    public String chatRoom() {
        return "CHAT";
    }
}
