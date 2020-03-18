package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ShopPurchaseCommand::class.java.name)

object ShopPurchaseCommand : Command {

    override val prefix: String = "shop"

    override val syntax: String = "[thing] - purchase thing"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (GameState.currentScreen != AbstractDungeon.CurrentScreen.SHOP) {
            logger.info("Shop is not opened")
            return false
        }
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val thing = parameters[0]
        val position = thing % 10

        val hb = when {
            thing >= 50 || thing < 10 -> null
            thing >= 40 -> {
                SpireInternals.shopPotions
                    .find { SpireInternals.storePotionSlot(it) == position - 1 }?.potion?.hb
            }
            thing >= 30 -> {
                SpireInternals.shopRelics
                    .find { SpireInternals.storeRelicSlot(it) == position - 1 }?.relic?.hb
            }
            thing >= 20 -> AbstractDungeon.shopScreen.colorlessCards?.getByPosition(position)?.hb
            thing >= 10 -> AbstractDungeon.shopScreen.coloredCards?.getByPosition(position)?.hb
            else -> null
        }

        if (hb == null) {
            logger.info("There is no such thing {}", thing)
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
}
