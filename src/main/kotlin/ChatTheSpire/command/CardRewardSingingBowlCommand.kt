package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CardRewardSingingBowlCommand::class.java.name)

object CardRewardSingingBowlCommand : Command {

    override val prefix: String = "bowl"

    override val syntax: String = "bowl - singing bowl"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (GameState.currentScreen != AbstractDungeon.CurrentScreen.CARD_REWARD) {
            logger.info("Not in card reward screen")
            return false
        }

        val button = SpireInternals.cardRewardBowlButton
        if (button == null) {
            logger.info("Singing bowl button is null")
            return false
        }

        if (button.isHidden) {
            logger.info("Singing bowl button is hidden")
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(button.hb)
                Automation.rest()
            }
        }

        return true
    }
}
