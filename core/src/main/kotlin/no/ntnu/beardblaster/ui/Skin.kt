package no.ntnu.beardblaster.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.*
import no.ntnu.beardblaster.ElementType
import no.ntnu.beardblaster.assets.AtlasAsset
import no.ntnu.beardblaster.assets.FontAsset
import no.ntnu.beardblaster.assets.get

enum class Image(val atlasKey: String) {
    // Buttons
    Button("button_default"),
    ButtonHover("button_default_hover"),
    ButtonPressed("button_default_pressed"),

    // Dialog buttons
    DialogButtonOK("modal_fancy_header_button_green_check"),
    DialogButtonOKDisabled("modal_fancy_header_button_green_check_disabled"),
    DialogButtonCancel("modal_fancy_header_button_red_cross_left"),
    DialogButtonCancelDisabled("modal_fancy_header_button_red_cross_left_disabled"),

    // Input elements
    InputDark("input_texture_dark"),
    InputLight("input_texture_light"),

    // Backgrounds
    Background("background"),
    Modal("modal_fancy"),
    ModalSkull("modal_fancy_skull"),
    ModalHeader("modal_fancy_header"),

    // Spellbar and Elements
    ElementFire("element_fire"),
    ElementIce("element_ice"),
    ElementNature("element_nature"),
    SpellBarSlot("button_icon"),
}

operator fun Skin.get(image: Image): Drawable = this.getDrawable(image.atlasKey)

enum class FontStyle {
    Default
}

operator fun Skin.get(font: FontStyle): BitmapFont = this.getFont(font.name)

enum class LabelStyle {
    Heading,
    Body,
    Error
}

enum class ButtonStyle {
    OK,
    Cancel,
}

enum class Style {
    Default,
    Dialog,
}

fun createSkin(assets: AssetStorage): Skin {
    Scene2DSkin.defaultSkin = skin(assets[AtlasAsset.UI]) { skin ->
        add(FontStyle.Default.name, assets[FontAsset.Default])

        label {
            font = skin[FontStyle.Default]
            fontColor = Color.BLACK
        }

        label(LabelStyle.Heading.name) {
            font = skin[FontStyle.Default]
            fontColor = Color.BROWN
            background = skin[Image.ModalHeader]
        }
        label(LabelStyle.Body.name) {
            font = skin[FontStyle.Default]
            fontColor = Color.WHITE
        }

        label(LabelStyle.Error.name) {
            font = skin[FontStyle.Default]
            fontColor = Color.ORANGE
        }

        button(ButtonStyle.OK.name) {
            up = skin[Image.DialogButtonOK]
            down = skin[Image.DialogButtonOK]
            disabled = skin[Image.DialogButtonOKDisabled]
        }

        button(ButtonStyle.Cancel.name) {
            up = skin[Image.DialogButtonCancel]
            down = skin[Image.DialogButtonCancel]
            disabled = skin[Image.DialogButtonCancelDisabled]
        }

        button(ElementType.Fire.name) {
            up = skin[Image.ElementFire]
            down = skin[Image.ElementFire]
        }

        button(ElementType.Ice.name) {
            up = skin[Image.ElementIce]
            down = skin[Image.ElementIce]
        }

        button(ElementType.Nature.name) {
            up = skin[Image.ElementNature]
            down = skin[Image.ElementNature]
        }

        textButton {
            font = skin[FontStyle.Default]
            up = skin[Image.Button]
            over = skin[Image.ButtonHover]
            down = skin[Image.ButtonPressed]
            pressedOffsetX = 4f
            pressedOffsetY = 4f
        }

        textField {
            font = skin[FontStyle.Default]
            fontColor = Color.DARK_GRAY
            messageFontColor = Color.BROWN
            background = skin[Image.InputLight]
            focusedBackground = skin[Image.InputDark]
        }

        window {
            titleFont = skin[FontStyle.Default]
        }

        window(Style.Dialog.name) {
            titleFont = skin[FontStyle.Default]
            titleFontColor = Color.BROWN
            stageBackground = dimmedBackground()
        }
    }
    return Scene2DSkin.defaultSkin
}

private fun dimmedBackground(): Drawable {
    return SpriteDrawable(
        Sprite(
            Texture(
                Pixmap(
                    Gdx.graphics.width,
                    Gdx.graphics.height,
                    Pixmap.Format.RGB888
                )
            )
        ).apply {
            color = Color(1f, 1f, 1f, 0.85f)
        }
    )
}
