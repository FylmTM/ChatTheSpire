package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(DiscardPileCommand::class.java.name)

object DiscardPileCommand : Command {

    override val prefix: String = "s"

    override val syntax: String = "s - open discard pile"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.isNotEmpty()) {
            logger.info("Parameters are not supported")
            return false
        }
        if (AbstractDungeon.overlayMenu.discardPilePanel.isHidden) {
            logger.info("Discard pile panel is hidden")
            return false
        }

        if (doAction) {
            Job.execute {
                SpireInternals.discardPilePanelHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
