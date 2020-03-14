package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CombatRewardSelectCommand::class.java.name)

object CombatRewardSelectCommand : Command {
    override val prefix: String = "reward"

    override val syntax: String = "[reward] - select reward"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val rewardPosition = parameters[0]

        val reward = AbstractDungeon.combatRewardScreen?.rewards?.getByPosition(rewardPosition)

        if (reward == null) {
            logger.info("Invalid reward position: {}", rewardPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(reward.hb)
                Automation.rest()
            }
        }

        return true
    }
}
