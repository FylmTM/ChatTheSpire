package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.rooms.TreasureRoom
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(TreasureChestOpenCommand::class.java.name)

object TreasureChestOpenCommand : Command {

    override val prefix: String = "t"

    override val syntax: String = "t - open treasure chest"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.isNotEmpty()) {
            logger.info("Parameters are not supported")
            return false
        }
        val chest = when (val room = SafeSpire.room) {
            is TreasureRoom -> room.chest
            is TreasureRoomBoss -> room.chest
            else -> null
        }

        if (chest == null) {
            logger.info("We are not in treasure room")
            return false
        }

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
