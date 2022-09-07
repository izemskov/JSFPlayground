package ru.develgame.jsfgame

import ru.develgame.jsfgame.player.PlayerBean
import java.io.Serializable
import javax.ejb.EJB
import javax.enterprise.context.SessionScoped
import javax.inject.Inject
import javax.inject.Named

@Named("createPerson")
@SessionScoped
open class CreatePersonBean : Serializable {
    @Inject
    private lateinit var playerBean: PlayerBean

    @EJB
    private lateinit var chatBean: ChatBean

    open fun getPerson() = playerBean.getPerson()

    open fun createPerson(): String {
        if (playerBean.getPerson().name.isNullOrEmpty())
            playerBean.getPerson().name = playerBean.getPerson().uuid

        chatBean.personJoined(playerBean.getPerson())

        return "GAME"
    }
}