package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager
import java.awt.event.KeyEvent

private val logger = LogManager.getLogger(CardCommand::class.java.name)

object CardCommand : Command {

    override val prefix: String = "card"

    override val syntax: String = "[card] - play card\n[card] [monster] - play card on monster"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size !in 1..2) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        if (AbstractDungeon.isScreenUp) {
            logger.info("There is screen up")
            return false
        }

        val cardPosition = parameters[0]
        val monsterPosition = parameters.getOrNull(1)

        val trueCardPosition = if (cardPosition == 0) 10 else cardPosition
        val card = SafeSpire.hand?.getByPosition(trueCardPosition)
        if (card == null) {
            logger.info("Invalid card position: {}", cardPosition)
            return false
        }

        val monster = SafeSpire.monsters?.getByPosition(monsterPosition)

        if (!card.canUse(SafeSpire.player, monster)) {
            logger.info("Card {} is unusable", card.name)
            return false
        }

        if (
            (card.target == CardTarget.ENEMY || card.target == CardTarget.SELF_AND_ENEMY)
            && monster == null
        ) {
            logger.info("Card target requires enemy, but no monster selected")
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
                (monster?.hb ?: SafeSpire.player?.hb)
                    ?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
