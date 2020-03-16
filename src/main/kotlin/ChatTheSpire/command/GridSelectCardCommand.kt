package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(GridSelectCardCommand::class.java.name)

object GridSelectCardCommand : Command {

    override val prefix: String = "grid"

    override val syntax: String = "[card] - select card"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        if (AbstractDungeon.gridSelectScreen?.confirmScreenUp == true) {
            logger.info("Grid confirm screen is up")
            return false
        }

        val cardPosition = parameters[0]

        val firstCard = AbstractDungeon.gridSelectScreen?.targetGroup?.group?.getByPosition(1)
        val card = AbstractDungeon.gridSelectScreen?.targetGroup?.group?.getByPosition(cardPosition)
        if (card == null) {
            logger.info("Invalid grid card position: {}", cardPosition)
            return false
        }
        if (firstCard == null) {
            logger.info("There is no cards in grid selection")
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.rest()
                Automation.click()

                Automation.scrollTo(card.hb)
                Automation.click(card.hb)
            }
        }

        return true
    }
}
