package no.ntnu.beardblaster.spell

import ktx.log.info
import ktx.log.logger
import no.ntnu.beardblaster.commons.spell.SpellAction
import no.ntnu.beardblaster.user.UserData
import no.ntnu.beardblaster.wizard.WizardTextures

private val LOG = logger<SpellActionWithAnimation>()

class SpellActionWithAnimation(spellAction: SpellAction) : SpellAction(
    spell = spellAction.spell,
    receiver = spellAction.receiver,
    caster = spellAction.caster,
) {

    init {
        super.docId = spellAction.docId
        super.damageDealt = spellAction.damageDealt
        super.damageAbsorbed = spellAction.damageAbsorbed
        super.healing = spellAction.healing
        super.receiverWizard = spellAction.receiverWizard
        super.casterWizard = spellAction.casterWizard
        super.isForfeit = spellAction.isForfeit
    }

    fun determineMyAnimation() : WizardTextures {
        if (receiverWizard!!.isWizardDefeated() || casterWizard!!.isWizardDefeated()) {
            if (receiverWizard!!.isWizardDefeated()) {
                if (receiver == UserData.instance.user!!.id) {
                    return WizardTextures.GoodWizardDeath
                } else {
                    return WizardTextures.GoodWizardJump
                }
            }
            if (casterWizard!!.isWizardDefeated()) {
                if (caster == UserData.instance.user!!.id) {
                    return WizardTextures.GoodWizardDeath
                } else {
                    return WizardTextures.GoodWizardJump
                }
            }
        } else if (damageDealt > 0) {
            LOG.info { "Animation includes damage!" }

            if (receiver == UserData.instance.user!!.id) {
                return WizardTextures.GoodWizardHit
            } else {
                return WizardTextures.GoodWizardAttack1
            }
        } else if (healing > 0 && damageDealt == 0) {
            LOG.info { "Animation includes healing!" }

            if (receiver == UserData.instance.user!!.id) {
                return WizardTextures.GoodWizardIdle
            } else {
                return WizardTextures.GoodWizardJump
            }
        } else if (damageAbsorbed > 0) {
            LOG.info { "Animation includes absorbed!" }

            if (receiver == UserData.instance.user!!.id) {
                return WizardTextures.GoodWizardJump
            } else {
                return WizardTextures.GoodWizardIdle
            }
        }

        return WizardTextures.GoodWizardIdle
    }

    fun determineEnemyAnimation() : WizardTextures {
        if (receiverWizard!!.isWizardDefeated() || casterWizard!!.isWizardDefeated()) {

            if (receiverWizard!!.isWizardDefeated()) {
                if (receiver == UserData.instance.user!!.id) {
                    return WizardTextures.EvilWizardIdle
                } else {
                    return WizardTextures.EvilWizardDeath
                }
            }
            if (casterWizard!!.isWizardDefeated()) {
                if (caster == UserData.instance.user!!.id) {
                    return WizardTextures.EvilWizardIdle
                } else {
                    return WizardTextures.EvilWizardDeath
                }
            }
        } else if (damageDealt > 0) {
            LOG.info { "Animation includes damage!" }

            if (receiver == UserData.instance.user!!.id) {
                return WizardTextures.EvilWizardAttack
            } else {
                return WizardTextures.EvilWizardTakeHit
            }
        } else if (healing > 0 && damageDealt == 0) {
            LOG.info { "Animation includes healing!" }

            if (receiver == UserData.instance.user!!.id) {
                return WizardTextures.EvilWizardIdle
            } else {
                return WizardTextures.EvilWizardIdle
            }
        } else if (damageAbsorbed > 0) {
            LOG.info { "Animation includes absorbed!" }

            if (receiver == UserData.instance.user!!.id) {
                return WizardTextures.EvilWizardIdle
            } else {
                return WizardTextures.EvilWizardIdle
            }
        }
        return WizardTextures.EvilWizardIdle
    }

    override fun toString(): String {
        var text = "${casterWizard?.displayName} is casting ${spell.spellName}."

        if(spell.spellMitigation > 0 && spell.spellDamage == 0 && spell.spellHealing == 0) {
            text += " It reduces the next damage by ${spell.spellMitigation} (${spell.duration} round(s))."
        }


        if (damageAbsorbed > 0 && damageAbsorbed >= spell.spellDamage && spell.spellDamage > 0) {
            text += " All damage was absorbed by ${receiverWizard?.displayName}!"
        }

        if (healing > 0) {
            text += " It healed for $healing!"
        }

        if (damageDealt > 0) {
            if (damageAbsorbed > 0 && damageDealt > 0)  {
                text += " ${casterWizard?.displayName} dealt $damageDealt damage ($damageAbsorbed damage was absorbed)"
            } else  {
                if(spell.spellDamage > damageAbsorbed) {
                        text += " It dealt $damageDealt damage!"
                } else if( healing > 0) {
                    text += " It also dealt $damageDealt damage!"
                }
            }
        }

        return text
    }
}
