package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.ShopRoom
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(MerchantOpenShopCommand::class.java.name)

object MerchantOpenShopCommand : Command {

    override val prefix: String = "m"

    override val syntax: String = "m - ask merchant to open shop"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.isNotEmpty()) {
            logger.info("Parameters are not supported")
            return false
        }
        if (AbstractDungeon.isScreenUp) {
            logger.info("There is screen up")
            return false
        }

        val room = SafeSpire.room
        if (room !is ShopRoom) {
            logger.info("Not in shop room")
            return false
        }

        val merchant = room.merchant
        if (merchant == null) {
            logger.info("There are no merchant in shop room")
            return false
        }

        if (doAction) {
            Job.execute {
                merchant.hb?.let {
                    Automation.click(it)
                    Automation.rest()
                }
            }
        }

        return true
    }
}
