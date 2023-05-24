package ru.develgame.jsfgame;

import ru.develgame.jsfgame.domain.Person;
import ru.develgame.jsfgame.player.PlayerBean;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("createPerson")
@SessionScoped
public class CreatePersonBean implements Serializable {
    @Inject
    private PlayerBean playerBean;

    @EJB
    private transient ChatBean chatBean;

    private static final long serialVersionUID = 9138108733190235780L;

    public Person getPerson() {
        return playerBean.getPerson();
    }

    public String createPerson() {
        if (playerBean.getPerson().getName() == null || playerBean.getPerson().getName().isEmpty()) {
            playerBean.getPerson().setName(playerBean.getPerson().getUuid());
        }

        chatBean.personJoined(playerBean.getPerson());

        return "GAME";
    }

    public String chatRoom() {
        throw new RuntimeException("Test");
    }
}
