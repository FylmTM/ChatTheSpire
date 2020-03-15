package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.rooms.RestRoom
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ProceedCommand::class.java.name)

object ProceedCommand : Command {

    override val prefix: String = "n"

    override val syntax: String = "n - Next/Proceed/Skip Something"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        val hb = overlayProceedHitbox() ?: restRoomProceed()

        if (hb == null) {
            logger.info("None of proceed buttons exists")
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(hb)
                Automation.rest()
            }
        }

        return true
    }

    private fun overlayProceedHitbox(): Hitbox? {
        if (SpireInternals.proceedIsHidden) {
            logger.info("Proceed is hidden")
            return null
        }
        return SpireInternals.proceedHitbox
    }

    private fun restRoomProceed(): Hitbox? {
        val room = SafeSpire.room
        if (room is RestRoom) {
            if (room.campfireUI.confirmButton.isDisabled || SpireInternals.confirmButtonIsHidden(room.campfireUI.confirmButton)) {
                return null
            }
            return room.campfireUI.confirmButton.hb
        }
        return null
    }
}
