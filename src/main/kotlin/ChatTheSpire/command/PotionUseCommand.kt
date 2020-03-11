package ChatTheSpire.command

import ChatTheSpire.control.Control
import ChatTheSpire.control.Internals
import ChatTheSpire.control.Job
import ChatTheSpire.util.Spire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.potions.PotionSlot
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(PotionUseCommand::class.java.name)

object PotionUseCommand : Command {

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size !in 1..2) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val potionPosition = parameters[0]
        val monsterPosition = parameters.getOrNull(1)

        val potion = Spire.potions?.getByPosition(potionPosition)
        if (potion == null) {
            logger.info("Invalid potion position: {}", potionPosition)
            return false
        }

        if (potion is PotionSlot) {
            logger.info("No potion in a slot with position: {}", potionPosition)
            return false
        }

        val monster = Spire.monsters?.getByPosition(monsterPosition)

        if (potion.targetRequired && monster == null) {
            logger.info("Target required for potion with index: {}", potionPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Control.click(potion.hb)
                Internals.potionUseHitbox?.let(Control::click)

                if (potion.targetRequired) {
                    monster?.hb?.let(Control::click)
                    Control.rest()
                }
            }
        }

        return true
    }
}
