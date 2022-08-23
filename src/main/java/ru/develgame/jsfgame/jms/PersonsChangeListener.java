package ru.develgame.jsfgame.jms;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.jms.*;
import java.util.LinkedList;
import java.util.List;

@Named
@ApplicationScoped
public class PersonsChangeListener {
    @Resource(lookup = "jms/JSFGameConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/JSFGameTopic")
    private Topic topic;

    private Connection connection;
    private Session session;

    private List<MessageConsumer> consumers = new LinkedList<>();

    @PostConstruct
    public void init() {
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            connection.start();
        } catch (JMSException e) {
            // TODO
        }
    }

    @PreDestroy
    public void finite() {
        try {
            for (MessageConsumer consumer : consumers) {
                consumer.close();
            }

            session.close();
            connection.close();
        } catch (JMSException e) {
            // TODO
        }
    }

    public void addListener(MessageListener messageListener) {
        try {
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(messageListener);
            consumers.add(consumer);
        } catch (JMSException e) {
            // TODO
        }
    }
}
