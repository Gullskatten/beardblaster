package no.ntnu.beardblaster.spell

import ktx.log.info
import ktx.log.logger
import no.ntnu.beardblaster.commons.spell.AbstractSpellRepository
import no.ntnu.beardblaster.commons.spell.Element
import no.ntnu.beardblaster.commons.spell.Spell
import pl.mk5.gdx.fireapp.PlatformDistributor

private val LOG = logger<SpellRepository>()

class SpellRepository : PlatformDistributor<AbstractSpellRepository>(), AbstractSpellRepository {
    override fun getIOSClassName(): String {
        TODO("Not yet implemented")
    }

    override fun getAndroidClassName(): String {
       return "no.ntnu.beardblaster.dbclasses.SpellController"
    }

    override fun getWebGLClassName(): String {
        TODO("Not yet implemented")
    }

    override fun getSpellById(id: Int): Spell? {
        LOG.info { "Retrieving spell by ID: $id"}
       return platformObject.getSpellById(id)
    }

    override fun getAllElements(): List<Element> {
        return platformObject.getAllElements()
    }
}
