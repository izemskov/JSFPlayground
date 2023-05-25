package ru.develgame.jsfgame;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Singleton
@Startup
public class DerbyDBAutostart {
    @PostConstruct
    public void init() {
        try { Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Logger.getLogger(DerbyDBAutostart.class.getName()).info("Started DERBY in embedded+network mode");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DerbyDBAutostart.class.getName()).severe("NOT Started DERBY in embedded+network mode");
        }
    }
}
