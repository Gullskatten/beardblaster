package no.ntnu.beardblaster.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.scene2d.*
import no.ntnu.beardblaster.BeardBlasterGame
import no.ntnu.beardblaster.HEIGHT
import no.ntnu.beardblaster.WIDTH
import no.ntnu.beardblaster.user.UserAuth

class MenuScreen(
    game: BeardBlasterGame,
    batch: Batch,
    assets: AssetManager,
    camera: OrthographicCamera,
) : BaseScreen(game, batch, assets, camera) {
    private val skin: Skin = Scene2DSkin.defaultSkin
    private val createGameBtn = scene2d.textButton("Create Game")
    private val joinGameBtn = scene2d.textButton("Join Game")
    private val highScoreBtn = scene2d.textButton("LeaderBeard")
    private val tutorialBtn = scene2d.textButton("Tutorial")
    private val logoutBtn = scene2d.textButton("Log Out")
    private val exitBtn = scene2d.textButton("Exit Game")

    private val stage: Stage by lazy {
        val result = BeardBlasterStage()
        Gdx.input.inputProcessor = result
        result
    }

    override fun show() {
        setBtnEventListeners()
        val heading = scene2d.label("Welcome Wizard", "heading") {
            setAlignment(Align.center)
            setFontScale(2f)
        }
        val table = scene2d.table(skin) {
            setBounds(0f, 0f, WIDTH, HEIGHT)
            defaults().pad(20f)
            background = skin.getDrawable("background")
            add(heading).colspan(4).center()
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
        Gdx.input.inputProcessor = stage
    }

    override fun update(delta: Float) {}

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        update(delta)
        stage.act(delta)
        stage.draw()
    }

    override fun setBtnEventListeners() {
        createGameBtn.onClick {
            // Handle creation of game, and then go to Lobby screen to display code and wait for player 2
            game.setScreen<LobbyScreen>()
        }
        joinGameBtn.onClick {
            game.setScreen<JoinLobbyScreen>()
        }
        highScoreBtn.onClick {
            game.setScreen<HighscoreScreen>()
        }
        tutorialBtn.onClick {
            game.setScreen<TutorialScreen>()
        }
        logoutBtn.onClick {
            if (UserAuth().isLoggedIn()) {
                UserAuth().signOut()
            }
            game.setScreen<LoginMenuScreen>()
        }
        exitBtn.onClick {
            Gdx.app.exit()
        }
    }
}