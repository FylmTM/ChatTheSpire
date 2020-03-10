package ChatTheSpire.command

import ChatTheSpire.control.Control
import ChatTheSpire.control.Job
import ChatTheSpire.util.Spire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.potions.PotionSlot
import com.megacrit.cardcrawl.ui.panels.PotionPopUp
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(PotionDestroyCommand::class.java.name)

object PotionDestroyCommand : Command {

    override fun perform(parameters: List<Int>): Boolean =
        execute(parameters = parameters, doAction = true)

    override fun canPerform(parameters: List<Int>): Boolean =
        execute(parameters = parameters, doAction = false)

    private fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val potionPosition = parameters[0]

        val potion = Spire.potions?.getByPosition(potionPosition)
        if (potion == null) {
            logger.info("Invalid potion position: {}", potionPosition)
            return false
        }

        if (potion is PotionSlot) {
            logger.info("No potion in a slot with position: {}", potionPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Control.click(potion.hb)

                val field = PotionPopUp::class.java.getDeclaredField("hbBot")
                field.isAccessible = true
                val destroyHitbox = field.get(AbstractDungeon.topPanel.potionUi) as Hitbox
                Control.click(destroyHitbox)
            }
        }

        return true
    }
}
