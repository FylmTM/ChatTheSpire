package ChatTheSpire.command

import ChatTheSpire.control.Control
import ChatTheSpire.control.Job
import ChatTheSpire.util.Spire
import ChatTheSpire.util.getByPosition
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CardCommand::class.java.name)

object CardCommand : Command {

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size !in 1..2) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val cardPosition = parameters[0]
        val monsterPosition = parameters.getOrNull(1)

        val card = Spire.hand?.getByPosition(cardPosition)
        if (card == null) {
            logger.info("Invalid card position: {}", cardPosition)
            return false
        }

        val monster = Spire.monsters?.getByPosition(monsterPosition)

        if (!card.canUse(Spire.player, monster)) {
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
                Control.click(card.hb)
                (monster?.hb ?: Spire.player?.hb)
                    ?.let(Control::click)
            }
        }

        return true
    }
}
