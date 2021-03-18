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

private val LOG = logger<RegisterScreen>()
class RegisterScreen(game: BeardBlasterGame) : AbstractScreen(game) {

    private val registrationStage: Stage by lazy {
        val result = Stage(FitViewport(worldWidth, worldHeight))
        Gdx.input.inputProcessor = result
        result
    }


    private lateinit var skin: Skin

    private lateinit var table: Table
    private lateinit var leftTable: Table
    private lateinit var rightTable: Table

    private lateinit var heading: Label

    private lateinit var createButton: TextButton
    private lateinit var backButton: Button

    private lateinit var userNameInput: TextField
    private lateinit var emailInput: TextField
    private lateinit var passwordInput: TextField
    private lateinit var rePasswordInput: TextField
    private var hasRegistered = false
    override fun show() {
        LOG.debug { "Registration Screen Shown" }
        Gdx.input.inputProcessor = registrationStage
        skin = Skin(Assets.assetManager.get(Assets.atlas))
        table = Table(skin)
        table.setBounds(0f,0f, viewport.worldWidth, viewport.worldHeight)
        rightTable = Table(skin)
        leftTable = Table(skin)

        val standardFont = Assets.assetManager.get(Assets.standardFont)

        Label.LabelStyle(standardFont, Color.BLACK).also {
            heading = Label("Create Wizard", it)
            heading.setFontScale(2f)
            it.background = skin.getDrawable("modal_fancy_header")
            heading.setAlignment(Align.center)
        }

        val createUserButtonStyle = TextButton.TextButtonStyle()
        skin.getDrawable("button_default_pressed").also { createUserButtonStyle.down = it }
        skin.getDrawable("button_default").also { createUserButtonStyle.up = it }

        val backButtonStyle =  Button.ButtonStyle()
        skin.getDrawable("modal_fancy_header_button_red_cross_left").also {
            backButtonStyle.down = it
            backButtonStyle.up = it
        }

        standardFont.apply {
            createUserButtonStyle.font = this
        }
        backButton = Button(backButtonStyle)
        createButton = TextButton("CREATE WIZARD", createUserButtonStyle)

        val textInputStyle = TextField.TextFieldStyle()

        textInputStyle.also {
            it.background = skin.getDrawable("input_texture_dark")
            it.fontColor = Color.BROWN
            it.font = standardFont
            it.messageFontColor = Color.GRAY
        }

        userNameInput = TextField("", textInputStyle)
        userNameInput.messageText = "Enter wizard name.."
        emailInput = TextField("", textInputStyle)
        emailInput.messageText = "Enter e-mail address.."
        passwordInput = TextField("", textInputStyle)
        passwordInput.setPasswordCharacter("*"[0])
        passwordInput.isPasswordMode = true
        passwordInput.messageText = "Enter password.."
        rePasswordInput = TextField("", textInputStyle)
        rePasswordInput.setPasswordCharacter("*"[0])
        rePasswordInput.messageText = "Re-enter password.."
        rePasswordInput.isPasswordMode = true


        //%TODO(find out why input fields renders with wrong width)
        //Creating table

        rightTable.apply {
            this.defaults().pad(30f)
            this.background = skin.getDrawable("modal_fancy")
            this.add(heading)
            this.row()
            this.add(userNameInput).width(570f)
            this.row()
            this.add(emailInput).width(570f)
            this.row()
            this.add(passwordInput).width(570f)
            this.row()
            this.add(rePasswordInput).width(570f)
            this.row()
            this.add(createButton).width(370f)
        }

        val stack = Stack()
        stack.add(backButton)

        leftTable.apply {
            this.top()
            this.add(stack).fillY().align(Align.top).padTop(50f)
        }



        table.apply {
            this.background = skin.getDrawable("background")
            this.add(leftTable).width(91f).expandY().fillY()
            this.add(rightTable).width(viewport.worldWidth * 0.9f).fillY()
        }

        // Adding actors to the stage
        registrationStage.addActor(table)
    }

    override fun update(delta: Float) {
        createButton.onClick {
            /*if(!hasRegistered) {
                UserAuth().createUser(emailInput.text, passwordInput.text, userNameInput.text)
                hasRegistered = true
            }*/
            game.setScreen<LoginMenuScreen>()
        }
        backButton.onClick {
            game.setScreen<LoginMenuScreen>()
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        update(delta)

        registrationStage.act(delta)
        registrationStage.draw()

    }

    override fun resize(width: Int, height: Int) {

    }
}
