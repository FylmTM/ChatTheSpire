package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ExhaustPileCommand::class.java.name)

object ExhaustPileCommand : Command {

    override val prefix: String = "x"

    override val syntax: String = "x - open exhausted cards"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.isNotEmpty()) {
            logger.info("Parameters are not supported")
            return false
        }
        if (AbstractDungeon.overlayMenu.exhaustPanel.isHidden) {
            logger.info("Exhausted pile panel is hidden")
            return false
        }

        if (AbstractDungeon.player.exhaustPile.isEmpty) {
            logger.info("Exhaust pile is empty")
            return false
        }

        if (doAction) {
            Job.execute {
                SpireInternals.exhaustPilePanelHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
