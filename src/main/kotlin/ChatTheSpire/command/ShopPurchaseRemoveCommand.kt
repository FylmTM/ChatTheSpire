package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ShopPurchaseRemoveCommand::class.java.name)

object ShopPurchaseRemoveCommand : Command {

    override val prefix: String = "r"

    override val syntax: String = "r - purchase remove card"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (GameState.currentScreen != AbstractDungeon.CurrentScreen.SHOP) {
            logger.info("Not in shop screen")
            return false
        }


        if (!AbstractDungeon.shopScreen.purgeAvailable) {
            logger.info("Purge is not available")
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.hover(SpireInternals.shopPurgeCardX.toInt(), SpireInternals.shopPurgeCardY.toInt())
                Automation.click()
                Automation.rest()
            }
        }

        return true
    }
}
