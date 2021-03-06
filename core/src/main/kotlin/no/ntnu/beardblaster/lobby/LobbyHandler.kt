package no.ntnu.beardblaster.lobby

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.error
import ktx.log.info
import ktx.log.logger
import no.ntnu.beardblaster.assets.Nls
import no.ntnu.beardblaster.commons.State
import no.ntnu.beardblaster.commons.game.Game
import no.ntnu.beardblaster.commons.game.GamePlayer
import no.ntnu.beardblaster.game.GameData
import no.ntnu.beardblaster.user.UserData
import java.util.*

private val LOG = logger<LobbyHandler>()

class LobbyHandler : Observable() {
    var game: Game? = null
        private set

    var error: String? = null
        private set

    var isLoading: Boolean = false
        private set
    var subscription: Job? = null

    fun setGame(game: Game?) {
        this.game = game
    }

    suspend fun createLobby() {
        game = null
        LobbyRepository().createLobby().collect {
            setChanged()
            when (it) {
                is State.Success -> {
                    LOG.info { "Notifying observers of lobby with code ${it.data.code}" }
                    notifyObservers(it.data)
                    setGame(it.data)
                    GameData.instance.isHost = true
                }
                is State.Loading -> {
                    notifyObservers(Nls.loading())
                }
                is State.Failed -> {
                    notifyObservers(it.message)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun joinLobbyWithCode(code: String) {
        subscription = KtxAsync.launch {
            LobbyRepository()
                .joinLobbyWithCode(
                    code,
                    GamePlayer.fromUser(UserData.instance.user!!)
                )
                .collect {
                    when (it) {
                        is State.Success -> {
                            LOG.info { "Joined lobby with id ${it.data.id}" }
                            notifyObservers(it.data)
                            game = it.data
                            setChanged()
                            isLoading = false
                            subscribeToUpdatesOn(it.data.id)
                        }
                        is State.Failed -> {
                            error = it.message
                            LOG.error { it.message }
                            notifyObservers(it.message)
                            setChanged()
                            isLoading = false
                        }
                        is State.Loading -> {
                            isLoading = true
                            setChanged()

                        }
                    }
                }
        }
    }

    @ExperimentalCoroutinesApi
    private fun subscribeToUpdatesOn(id: String) {
        LOG.info { "Subscribing to updates on $id" }
        subscription = KtxAsync.launch {
            LobbyRepository().subscribeToLobbyUpdates(id).collect {
                when (it) {
                    is State.Success -> {
                        game = it.data
                        notifyObservers(it.data)
                    }
                    is State.Loading -> {
                    }
                    is State.Failed -> {
                        LOG.error { it.message }
                        error = it.message
                        notifyObservers(error)
                    }
                }
                setChanged()
            }
        }
    }

    fun leaveLobby(): Flow<State<Boolean>>? {
        if (game != null && game!!.id.isNotEmpty()) {
            GameData.instance.isHost = false

            if(subscription != null && subscription!!.isActive) {
                subscription!!.cancel()
            }

            return LobbyRepository().leaveLobbyWithId(game!!.id)
        }
        return null
    }

    fun cancelLobby(): Flow<State<Boolean>>? {

        if(subscription != null && subscription!!.isActive) {
            subscription!!.cancel()
        }

        if (game != null && game!!.id.isNotEmpty()) {
            GameData.instance.isHost = false
            return LobbyRepository().cancelLobbyWithId(game!!.id)
        }
        return null
    }

    fun startGame(): Flow<State<Boolean>>? {
        if(subscription != null && subscription!!.isActive) {
            subscription!!.cancel()
        }

        if (game != null && game!!.id.isNotEmpty()) {
            return LobbyRepository().startGame(game!!.id)
        }
        return null
    }
}
