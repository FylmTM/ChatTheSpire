package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Internals
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.potions.PotionSlot
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(PotionDestroyCommand::class.java.name)

object PotionDestroyCommand : Command {

    override val prefix: String = "destroypotion"

    override val syntax: String = "destroypotion [slot]"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val potionPosition = parameters[0]

        val potion = SafeSpire.potions?.getByPosition(potionPosition)
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
                Automation.click(potion.hb)
                Internals.potionDestroyHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
