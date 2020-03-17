package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(MapBossCommand::class.java.name)

object MapBossCommand : Command {

    override val prefix: String = "boss"

    override val syntax: String = "boss - fight boss!"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (GameState.currentScreen != AbstractDungeon.CurrentScreen.MAP) {
            logger.info("Map is not opened")
            return false
        }

        if (!SafeSpire.isNextRoomBoss) {
            logger.info("Next room is not boss")
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.rest()
                Automation.click()
                val hb = AbstractDungeon.dungeonMapScreen?.map?.bossHb
                hb?.let(Automation::scrollTo)
                hb?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
