package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.SpireInternals
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.RestRoom
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(RestRoomOptionSelectCommand::class.java.name)

object RestRoomOptionSelectCommand : Command {

    override val prefix: String = "campfire"

    override val syntax: String = "[option] - select campfire option"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val room = SafeSpire.room

        if (room !is RestRoom) {
            logger.info("Room is not rest room")
            return false
        }

        if (room.campfireUI.somethingSelected) {
            logger.info("Something is already selected")
            return false
        }

        val restRoomOptionPosition = parameters[0]

        val options = SpireInternals.restRoomButtons(room.campfireUI)
        val option = options.getByPosition(restRoomOptionPosition)

        if (option == null) {
            logger.info("Invalid rest room option position: {}", restRoomOptionPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(option.hb)
                Automation.rest()
            }
        }

        return true
    }
}
