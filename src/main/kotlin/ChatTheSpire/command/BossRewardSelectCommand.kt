package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(BossRewardSelectCommand::class.java.name)

object BossRewardSelectCommand : Command {
    override val prefix: String = "bossreward"

    override val syntax: String = "[relic] - select relic reward"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        if (GameState.currentScreen != AbstractDungeon.CurrentScreen.BOSS_REWARD) {
            logger.info("Not in boss reward screen")
            return false
        }

        val bossRewardPosition = parameters[0]
        val relic = AbstractDungeon.bossRelicScreen?.relics?.getByPosition(bossRewardPosition)

        if (relic == null) {
            logger.info("Invalid relic reward position: {}", bossRewardPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(relic.hb)
                Automation.rest()
            }
        }

        return true
    }
}
