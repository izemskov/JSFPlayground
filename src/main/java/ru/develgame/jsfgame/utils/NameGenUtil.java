package ru.develgame.jsfgame.utils;

import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.NameGenerator;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class NameGenUtil {
    private NameGenerator generator = new NameGenerator();

    public String getName() {
        return generator.generateName(Gender.MALE).getFirstName();
    }
}
