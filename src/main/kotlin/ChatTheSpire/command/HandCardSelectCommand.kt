package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.getByPosition
import org.apache.logging.log4j.LogManager
import java.awt.event.KeyEvent

private val logger = LogManager.getLogger(CardCommand::class.java.name)

object HandCardSelectCommand : Command {

    override val prefix: String = "hand"

    override val syntax: String = "[card] - select card"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val cardPosition = parameters[0]

        val trueCardPosition = if (cardPosition == 0) 10 else cardPosition
        val card = SafeSpire.hand?.getByPosition(trueCardPosition)
        if (card == null) {
            logger.info("Invalid card position: {}", cardPosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.rest()
                Automation.click()
                Automation.keyPress(
                    when (cardPosition) {
                        1 -> KeyEvent.VK_1
                        2 -> KeyEvent.VK_2
                        3 -> KeyEvent.VK_3
                        4 -> KeyEvent.VK_4
                        5 -> KeyEvent.VK_5
                        6 -> KeyEvent.VK_6
                        7 -> KeyEvent.VK_7
                        8 -> KeyEvent.VK_8
                        9 -> KeyEvent.VK_9
                        0 -> KeyEvent.VK_0
                        else -> throw RuntimeException("Why is there more than 10 cards?")
                    }
                )
                Automation.rest()
            }
        }

        return true
    }
}
