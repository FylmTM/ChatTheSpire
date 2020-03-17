package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CancelCommand::class.java.name)

object CancelCommand : Command {

    override val prefix: String = "b"

    override val syntax: String = "b - back/return/cancel"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        val hb = overlayCancel() ?: bossRelicCancel()

        if (hb == null) {
            logger.info("None of cancel buttons exists")
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

    private fun overlayCancel(): Hitbox? {
        if (AbstractDungeon.overlayMenu.cancelButton.isHidden) {
            logger.info("Cancel is hidden")
            return null
        }
        return AbstractDungeon.overlayMenu.cancelButton.hb
    }

    private fun bossRelicCancel(): Hitbox? {
        if (GameState.currentScreen == AbstractDungeon.CurrentScreen.BOSS_REWARD) {
            if (SpireInternals.bossRelicCancelButton?.isHidden == true) {
                logger.info("Boss relic cancel is hidden")
                return null
            }
            return SpireInternals.bossRelicCancelButton?.hb
        }
        return null
    }
}
