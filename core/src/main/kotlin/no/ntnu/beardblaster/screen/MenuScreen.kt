package no.ntnu.beardblaster.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import kotlinx.coroutines.launch
import ktx.actors.onClick
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.info
import ktx.log.logger
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import no.ntnu.beardblaster.BeardBlasterGame
import no.ntnu.beardblaster.assets.Nls
import no.ntnu.beardblaster.commons.spell.Element
import no.ntnu.beardblaster.commons.spell.Spell
import no.ntnu.beardblaster.spell.SpellRepository
import no.ntnu.beardblaster.ui.*
import no.ntnu.beardblaster.user.UserAuth
import no.ntnu.beardblaster.user.UserData
import java.util.*

private val LOG = logger<MenuScreen>()

class MenuScreen(
    game: BeardBlasterGame,
    batch: SpriteBatch,
    assets: AssetStorage,
    camera: OrthographicCamera,
) : BaseScreen(game, batch, assets, camera), Observer {
    private lateinit var createGameBtn: TextButton
    private lateinit var joinGameBtn: TextButton
    private lateinit var highScoreBtn: TextButton
    private lateinit var tutorialBtn: TextButton
    private lateinit var logoutBtn: TextButton
    private lateinit var exitBtn: TextButton
    private lateinit var wizardHeading: Label
    private val currentWizardLabel: Label =
        bodyLabel(
            UserData.instance.getCurrentUserString(),
            1.5f,
            LabelStyle.BodyOutlined.name
        ) // Kept it here as it can crash for lateinit since loading user can finish before screen has been initialized

    override fun initScreen() {
        val selectedElements = listOf(Element(1, "spell"), Element(2, "2"), Element(2, "3"))
        val s = SpellRepository().getSpellById(selectedElements.map { elem: Element -> elem.elementID }.reduce { sum, element -> sum * element })
        LOG.info { "YOLO: ${s?.spellName} - ${s?.spellDescription} " }

        if (UserData.instance.user == null && !UserData.instance.isLoading) {
            KtxAsync.launch {
                UserData.instance.loadUserData()
            }
            UserData.instance.addObserver(this)
        }
        KtxAsync.launch {
            UserData.instance.loadUserData()
        }

        val foundSpell = SpellRepository().getSpellById(1)
        if(foundSpell != null) {
            LOG.info { foundSpell.spellName }
        } else {
            LOG.info { "Spell not found!" }
        }

        UserData.instance.addObserver(this)

        createGameBtn = scene2d.textButton(Nls.createGame())
        joinGameBtn = scene2d.textButton(Nls.joinGame())
        highScoreBtn = scene2d.textButton(Nls.leaderBeard())
        tutorialBtn = scene2d.textButton(Nls.tutorial())
        logoutBtn = scene2d.textButton(Nls.logOut())
        exitBtn = scene2d.textButton(Nls.exitGame())
        wizardHeading = headingLabel("BeardBlaster")

        val table = fullSizeTable(20f).apply {
            background = skin[Image.Background]
            add(wizardHeading).colspan(4).center()
            row()
            add(currentWizardLabel).colspan(4).center()
            row()
            add(createGameBtn).colspan(4).center()
            row()
            add(joinGameBtn).colspan(4).center()
            row()
            add(highScoreBtn).colspan(2).center()
            add(tutorialBtn).colspan(2).center()
            row()
            add(logoutBtn).colspan(2).center()
            add(exitBtn).colspan(2).center()
        }
        stage.addActor(table)
    }

    override fun setBtnEventListeners() {
        createGameBtn.onClick {
            // Handle creation of game, and then go to Lobby screen to display code and wait for player 2
            game.setScreen<GameplayScreen>()
            if (UserData.instance.user != null) {
                game.setScreen<LobbyScreen>()
            }
        }
        joinGameBtn.onClick {
            if (UserData.instance.user != null) {
                game.setScreen<JoinLobbyScreen>()
            }
        }
        highScoreBtn.onClick {
            if (UserData.instance.user != null) {
                game.setScreen<HighScoreScreen>()
            }
        }
        tutorialBtn.onClick {
            game.setScreen<TutorialScreen>()
        }
        logoutBtn.onClick {
            if (UserAuth().isLoggedIn()) {
                UserData.instance.setUserData(null)
                UserAuth().signOut()
            }
            game.setScreen<LoginMenuScreen>()
        }
        exitBtn.onClick {
            Gdx.app.exit()
        }
    }

    override fun update(delta: Float) {
    }

    override fun update(p0: Observable?, p1: Any?) {
        LOG.info { p1.toString() }
        currentWizardLabel.setText(p1.toString())
    }

    override fun dispose() {
        super.dispose()
        UserData.instance.deleteObserver(this)
    }
}
