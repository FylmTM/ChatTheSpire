package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(DeckCommand::class.java.name)

object DeckCommand : Command {

    override val prefix: String = "d"

    override val syntax: String = "d - open deck"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.isNotEmpty()) {
            logger.info("Parameters are not supported")
            return false
        }
        if (SpireInternals.deckButtonDisabled) {
            logger.info("Deck button is disabled")
            return false
        }

        if (doAction) {
            Job.execute {
                AbstractDungeon.topPanel.deckHb?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
