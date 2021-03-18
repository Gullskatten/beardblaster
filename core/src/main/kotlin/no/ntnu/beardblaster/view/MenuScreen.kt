package no.ntnu.beardblaster.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.actors.onClick
import ktx.log.debug
import ktx.log.logger
import no.ntnu.beardblaster.AbstractScreen
import no.ntnu.beardblaster.BeardBlasterGame
import no.ntnu.beardblaster.assets.Assets
import no.ntnu.beardblaster.worldHeight
import no.ntnu.beardblaster.worldWidth


private val LOG = logger<MenuScreen>()


class MenuScreen(game: BeardBlasterGame) : AbstractScreen(game) {

    private val menuStage: Stage by lazy {
        val result = Stage(FitViewport(worldWidth, worldHeight))
        Gdx.input.inputProcessor = result
        result
    }


    private lateinit var skin: Skin
    private lateinit var table: Table
    private lateinit var heading: Label

    private lateinit var createGameButton: TextButton
    private lateinit var joinGameButton: TextButton
    private lateinit var highscoreButton: TextButton
    private lateinit var tutorialButton: TextButton



    override fun show() {
        LOG.debug { "MENU Screen" }

        Gdx.input.inputProcessor = menuStage
        skin = Skin(Assets.assetManager.get(Assets.atlas))
        table = Table(skin)
        table.setBounds(0f,0f, viewport.worldWidth, viewport.worldHeight)

        val standardFont = Assets.assetManager.get(Assets.standardFont)

        Label.LabelStyle(standardFont, Color.BLACK).also {
            heading = Label("Welcome <Username>", it)
            heading.setFontScale(2f)
            it.background = skin.getDrawable("modal_fancy_header")
            heading.setAlignment(Align.center)
        }

        val createUserButtonStyle = TextButton.TextButtonStyle()
        skin.getDrawable("button_default_pressed").also { createUserButtonStyle.down = it }
        skin.getDrawable("button_default").also { createUserButtonStyle.up = it }

        standardFont.apply {
            createUserButtonStyle.font = this
        }

        createGameButton = TextButton("Create Game", createUserButtonStyle)
        joinGameButton = TextButton("Join Game", createUserButtonStyle)
        highscoreButton = TextButton("LeaderBeard", createUserButtonStyle)
        tutorialButton = TextButton("Tutorial", createUserButtonStyle)

        val textInputStyle = TextField.TextFieldStyle()

        textInputStyle.also {
            it.background = skin.getDrawable("input_texture_dark")
            it.fontColor = Color.BROWN
            it.font = standardFont
            it.messageFontColor = Color.GRAY
        }



        //%TODO(find out why input fields renders with wrong width)
        //Creating table
        table.apply {
            this.defaults().pad(30f)
            this.background = skin.getDrawable("background")
            this.add(heading)
            this.row()
            this.add(createGameButton)
            this.row()
            this.add(joinGameButton)
            this.row()
            this.add(highscoreButton)
            this.row()
            this.add(tutorialButton)
        }

        // Adding actors to the stage
        menuStage.addActor(table)


    }

    override fun update(delta: Float) {

        tutorialButton.onClick {
            game.setScreen<LoginMenuScreen>()
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        update(delta)

        menuStage.act(delta)
        menuStage.draw()

    }


}
