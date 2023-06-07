package ru.develgame.jsfgame.user;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
    public String getUsername() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
}
