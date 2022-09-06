package ru.develgame.jsfgame.ai;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;

/**
 * @author Ilya Zemskov
 */
@Singleton
@Startup
public class MonsterMover {
    @Inject
    private MonsterBean monsterBean;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void initialize() {
        timerService.createTimer( 200, "");
    }

    @Timeout
    public void monsterTimeout(Timer timer) {
        monsterBean.updatePerson();
    }
}
