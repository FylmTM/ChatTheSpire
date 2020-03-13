package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import org.apache.logging.log4j.LogManager
import java.awt.event.KeyEvent

private val logger = LogManager.getLogger(CardCommand::class.java.name)

object CardCommand : Command {

    override val prefix: String = "card"

    override val syntax: String = "card [position]\ncard [position] [monster]"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size !in 1..2) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val cardPosition = parameters[0]
        val monsterPosition = parameters.getOrNull(1)

        val card = SafeSpire.hand?.getByPosition(cardPosition)
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
                        10 -> KeyEvent.VK_0
                        else -> KeyEvent.VK_1
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
