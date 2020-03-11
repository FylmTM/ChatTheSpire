package ChatTheSpire.command

import ChatTheSpire.control.Control
import ChatTheSpire.control.Job
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.events.RoomEventDialog
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(PotionDestroyCommand::class.java.name)

object DialogCommand : Command {

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val optionPosition = parameters[0]

        val option = RoomEventDialog.optionList?.getByPosition(optionPosition)
        if (option == null) {
            logger.info("Invalid option position: {}", optionPosition)
            return false
        }
        if (option.isDisabled) {
            logger.info("Option[{}] is disabled")
            return false
        }

        if (doAction) {
            Job.execute {
                Control.click(option.hb)
                Control.rest()
            }
        }

        return true
    }
}
