package ru.develgame.jsfgame.jms;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class ChangesInformer {
    @Resource(lookup = "jms/JSFGameConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/JSFGameTopic")
    private Topic topic;

    @Inject
    private Logger logger;

    public void sendMessage() {
        try {
            try (Connection connection = connectionFactory.createConnection()) {
                try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                    try (MessageProducer producer = session.createProducer(topic)) {
                        TextMessage textMessage = session.createTextMessage();
                        textMessage.setText("Update");

                        producer.send(textMessage);
                    }
                }
            }
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Cannot send JMS message", e);
        }
    }
}
