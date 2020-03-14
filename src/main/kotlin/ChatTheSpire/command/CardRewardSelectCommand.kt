package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CombatRewardSelectCommand::class.java.name)

object CardRewardSelectCommand : Command {
    override val prefix: String = "cardreward"

    override val syntax: String = "[card] - select card reward"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val cardRewardPosition = parameters[0]

        val cardReward = AbstractDungeon.cardRewardScreen?.rewardGroup?.getByPosition(cardRewardPosition)

        if (cardReward == null) {
            logger.info("Invalid card reward position: {}", cardRewardPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(cardReward.hb)
                Automation.rest()
            }
        }

        return true
    }
}
