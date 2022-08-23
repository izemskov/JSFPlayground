package ru.develgame.jsfgame;

import ru.develgame.jsfgame.jms.PersonsChangeInformer;
import ru.develgame.jsfgame.jms.PersonsChangeListener;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import java.io.Serializable;

@Named("jmsTest")
@ViewScoped
public class JmsTestBean implements Serializable, MessageListener {
    @Inject
    private PersonsChangeInformer personsChangeInformer;

    @Inject
    private PersonsChangeListener personsChangeListener;

    @PostConstruct
    public void init() {
        personsChangeListener.addListener(this);
    }

    public void sendMessage() {
        personsChangeInformer.sendMessage();
    }

    @Override
    public void onMessage(Message message) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "Received message"));
    }
}
