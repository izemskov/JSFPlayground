package ru.develgame.jsfgame.jms;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.jms.*;

@Named
@ApplicationScoped
public class PersonsChangeInformer {
    @Resource(lookup = "jms/JSFGameConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/JSFGameTopic")
    private Topic topic;

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
            // TODO
        }
    }
}
