package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(DrawPileCommand::class.java.name)

object DrawPileCommand : Command {

    override val prefix: String = "a"

    override val syntax: String = "a - open draw pile"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (AbstractDungeon.overlayMenu.combatDeckPanel.isHidden) {
            logger.info("Draw pile panel is hidden")
            return false
        }

        if (doAction) {
            Job.execute {
                SpireInternals.drawPilePanelHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
