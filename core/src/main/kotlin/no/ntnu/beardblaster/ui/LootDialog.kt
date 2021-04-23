package no.ntnu.beardblaster.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.log.logger
import ktx.scene2d.*
import no.ntnu.beardblaster.WORLD_HEIGHT
import no.ntnu.beardblaster.WORLD_WIDTH
import no.ntnu.beardblaster.commons.game.Prize
import no.ntnu.beardblaster.user.UserData

private val log = logger<LootDialog>()

@Scene2dDsl
class LootDialog(
    loot: List<Prize>
) : Table(Scene2DSkin.defaultSkin), KTable {

    private val nameLabel: Label =
        scene2d.label("Loot", LabelStyle.LightText.name) {
            setAlignment(Align.center)
            setFontScale(2f)
        }

    private val yourLootLabel: Label = scene2d.label("You receive", LabelStyle.LightText.name) {
        setAlignment(Align.center)
        setFontScale(1.5f)
        wrap = true
    }

    private val opponentLootLabel: Label = scene2d.label("Opponent received", LabelStyle.LightText.name) {
        setAlignment(Align.center)
        setFontScale(1.5f)
        wrap = true
    }

    var closeBtn: Button = scene2d.textButton("Quit Game", ButtonStyle.Primary.name)

    companion object {
        const val PADDING = 20f
        const val LABEL_WIDTH = 860f
    }

    init {
        if(loot.isNotEmpty()) {
        background = skin[Image.ModalDark]
        pad(PADDING)
        add(nameLabel).center().top().padTop(PADDING).width(LABEL_WIDTH)
        row()
        yourLootLabel
        add(yourLootLabel).center().expand().width(LABEL_WIDTH)
        row()
        loot.filter { l -> l.receiver == UserData.instance.user!!.id }.forEach {
            val descLabel: Label = scene2d.label(it.item, LabelStyle.LightText.name) {
                setAlignment(Align.center)
                setFontScale(1.5f)
                wrap = true
                color = Color.valueOf("96ecff")
            }
            val amountLabel: Label = scene2d.label("x${it.amount}", LabelStyle.LightText.name) {
                setAlignment(Align.center)
                setFontScale(1.5f)
                wrap = true
                color = Color.valueOf("96ecff")
            }
            add(descLabel).left().expand().width(LABEL_WIDTH)
            add(amountLabel).right().expand().width(LABEL_WIDTH)
            row()
        }
        add(opponentLootLabel).center().expand().width(LABEL_WIDTH)
        row()
        loot.filter { l -> l.receiver != UserData.instance.user!!.id }.forEach {
            val descLabel: Label = scene2d.label(it.item, LabelStyle.LightText.name) {
                setAlignment(Align.center)
                setFontScale(1.5f)
                wrap = true
                color = Color.LIGHT_GRAY
            }
            val amountLabel: Label = scene2d.label("x${it.amount}", LabelStyle.LightText.name) {
                setAlignment(Align.center)
                setFontScale(1.5f)
                wrap = true
                color = Color.LIGHT_GRAY
            }
            add(descLabel).left().expand().width(LABEL_WIDTH)
            add(amountLabel).right().expand().width(LABEL_WIDTH)
            row()
        }
        add(closeBtn).center().bottom()
        pack()
        }
    }

    override fun getPrefWidth(): Float = WORLD_WIDTH * 0.5f
    override fun getPrefHeight(): Float = WORLD_HEIGHT * 0.6f
}

@Scene2dDsl
inline fun <S> KWidget<S>.lootDialog(
    loot: List<Prize>,
    init: LootDialog.(S) -> Unit = {},
): LootDialog = actor(LootDialog(loot), init)