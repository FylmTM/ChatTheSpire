package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Internals
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.potions.PotionSlot
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(PotionUseCommand::class.java.name)

object PotionUseCommand : Command {

    override val prefix: String = "usepotion"

    override val syntax: String = "usepotion [slot]\nusepotion [slot] [monster]"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size !in 1..2) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val potionPosition = parameters[0]
        val monsterPosition = parameters.getOrNull(1)

        val potion = SafeSpire.potions?.getByPosition(potionPosition)
        if (potion == null) {
            logger.info("Invalid potion position: {}", potionPosition)
            return false
        }

        if (potion is PotionSlot) {
            logger.info("No potion in a slot with position: {}", potionPosition)
            return false
        }

        val monster = SafeSpire.monsters?.getByPosition(monsterPosition)

        if (potion.targetRequired && monster == null) {
            logger.info("Target required for potion with index: {}", potionPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(potion.hb)
                Internals.potionUseHitbox?.let(Automation::click)

                if (potion.targetRequired) {
                    monster?.hb?.let(Automation::click)
                    Automation.rest()
                }
            }
        }

        return true
    }
}
