package ru.develgame.jsfgame

import java.util.logging.Logger
import javax.enterprise.inject.Produces
import javax.enterprise.inject.spi.InjectionPoint
import javax.inject.Named
import javax.inject.Singleton

@Named
@Singleton
open class LoggerProducer {
    @Produces
    open fun produceLogger(injectionPoint: InjectionPoint): Logger {
        return Logger.getLogger(javaClass.name)
    }
}