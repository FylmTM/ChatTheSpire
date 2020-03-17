package ChatTheSpire.command

import ChatTheSpire.GameState
import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.SpireInternals
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.rooms.RestRoom
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ProceedCommand::class.java.name)

object ProceedCommand : Command {

    override val prefix: String = "n"

    override val syntax: String = "n - next/proceed/confirm/skip"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        val hb = overlayProceed()
            ?: restRoomProceed()
            ?: cardRewardSkip()
            ?: gridConfirm()
            ?: handSelectConfirm()
            ?: bossRelicConfirm()

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

    private fun overlayProceed(): Hitbox? {
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

    private fun cardRewardSkip(): Hitbox? {
        if (GameState.currentScreen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
            val button = SpireInternals.cardRewardSkipButton
            if (button == null) {
                logger.info("Card reward skip button is null")
                return null
            }

            if (SpireInternals.skipButtonIsHidden(button)) {
                logger.info("Skip card button is hidden")
                return null
            }

            return button.hb
        }
        return null
    }

    private fun gridConfirm(): Hitbox? {
        if (GameState.currentScreen == AbstractDungeon.CurrentScreen.GRID) {
            val button = AbstractDungeon.gridSelectScreen.confirmButton

            if (button.isDisabled || SpireInternals.gridSelectConfirmButtonIsHidden) {
                logger.info("Grid select confirm button is disabled or hidden")
                return null
            }

            return button.hb
        }
        return null
    }

    private fun handSelectConfirm(): Hitbox? {
        if (GameState.currentScreen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
            if (SpireInternals.cardSelectConfirmButtonIsHidden) {
                logger.info("Hand select confirm button is hidden")
                return null
            }
            return AbstractDungeon.handCardSelectScreen.button.hb
        }
        return null
    }

    private fun bossRelicConfirm(): Hitbox? {
        if (GameState.currentScreen == AbstractDungeon.CurrentScreen.BOSS_REWARD) {
            val button = AbstractDungeon.bossRelicScreen?.confirmButton
            if (button == null || SpireInternals.confirmButtonIsHidden(button)) {
                logger.info("Boss relic confirm is hidden")
                return null
            }
            return button.hb
        }
        return null
    }
}
