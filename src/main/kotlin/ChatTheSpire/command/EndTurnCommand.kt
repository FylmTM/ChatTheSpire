package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(EndTurnCommand::class.java.name)

object EndTurnCommand : Command {

    override val prefix: String = "e"

    override val syntax: String = "e - end turn"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.isNotEmpty()) {
            logger.info("Parameters are not supported")
            return false
        }
        if (!AbstractDungeon.overlayMenu.endTurnButton.enabled) {
            logger.info("End turn button is not enabled")
            return false
        }

        if (doAction) {
            Job.execute {
                SpireInternals.endTurnHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
