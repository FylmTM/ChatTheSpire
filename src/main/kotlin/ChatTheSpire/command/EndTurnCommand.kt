package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Internals
import ChatTheSpire.util.Job
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(EndTurnCommand::class.java.name)

object EndTurnCommand : Command {

    override val prefix: String = "endturn"

    override val syntax: String = "endturn"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (!AbstractDungeon.overlayMenu.endTurnButton.enabled) {
            logger.info("End turn button is not enabled")
            return false
        }

        if (doAction) {
            Job.execute {
                Internals.endTurnHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
