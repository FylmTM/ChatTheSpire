package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CombatRewardSelectCommand::class.java.name)

object SkipCommand : Command {
    override val prefix: String = "k"

    override val syntax: String = "k - skip current screen"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (GameState.currentScreen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
            val button = SpireInternals.cardRewardSkipButton
            if (button == null) {
                logger.info("Card reward skip button is null")
                return false
            }

            if (SpireInternals.skipButtonIsHidden(button)) {
                logger.info("Skip card button is hidden")
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

        return false
    }
}
