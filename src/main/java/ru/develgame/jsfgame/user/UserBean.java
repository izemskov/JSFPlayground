package ru.develgame.jsfgame.user;

import ru.develgame.jsfgame.game.domain.Person;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private Person person;

    @PostConstruct
    private void init() {
        person.setName(getUsername());
    }

    public String getUsername() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

    public Person getPerson() {
        return person;
    }

    private static final long serialVersionUID = -7615739262702035246L;
}
