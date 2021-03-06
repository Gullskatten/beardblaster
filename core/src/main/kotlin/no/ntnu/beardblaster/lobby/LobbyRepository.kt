package no.ntnu.beardblaster.lobby

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import no.ntnu.beardblaster.commons.lobby.AbstractLobbyRepository
import no.ntnu.beardblaster.commons.State
import no.ntnu.beardblaster.commons.game.Game
import no.ntnu.beardblaster.commons.game.GamePlayer
import pl.mk5.gdx.fireapp.PlatformDistributor

class LobbyRepository : PlatformDistributor<AbstractLobbyRepository<Game>>(),
    AbstractLobbyRepository<Game> {

    override fun getIOSClassName(): String {
        TODO("Not yet implemented")
    }

    override fun getAndroidClassName(): String {
        return "no.ntnu.beardblaster.repositories.LobbyRepository"
    }

    override fun getWebGLClassName(): String {
        TODO("Not yet implemented")
    }

    override fun joinLobbyWithCode(code: String, opponent: GamePlayer): Flow<State<Game>> {
        return platformObject.joinLobbyWithCode(code, opponent)
    }

    override fun createLobby(): Flow<State<Game>> {
        return platformObject.createLobby()
    }

    override fun cancelLobbyWithId(id: String): Flow<State<Boolean>> {
        return platformObject.cancelLobbyWithId(id)
    }

    override fun startGame(id: String): Flow<State<Boolean>> {
        return platformObject.startGame(id)
    }

    @ExperimentalCoroutinesApi
    override fun subscribeToLobbyUpdates(id: String): Flow<State<Game>> {
        return platformObject.subscribeToLobbyUpdates(id)
    }

    override fun leaveLobbyWithId(id: String): Flow<State<Boolean>> {
        return platformObject.leaveLobbyWithId(id)
    }
}
