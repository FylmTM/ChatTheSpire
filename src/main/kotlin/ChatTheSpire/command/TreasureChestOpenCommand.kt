package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.rooms.TreasureRoom
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(EndTurnCommand::class.java.name)

object TreasureChestOpenCommand : Command {

    override val prefix: String = "t"

    override val syntax: String = "t - open treasure chest"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        val room = SafeSpire.room
        if (room !is TreasureRoom) {
            logger.info("Not in treasure room")
            return false
        }

        val chest = room.chest
        if (chest.isOpen) {
            logger.info("Chest is already open")
            return false
        }

        val hb = SpireInternals.chestHitbox(chest)

        if (doAction) {
            Job.execute {
                hb?.let {
                    Automation.click(hb)
                    Automation.rest()
                }
            }
        }

        return true
    }
}
