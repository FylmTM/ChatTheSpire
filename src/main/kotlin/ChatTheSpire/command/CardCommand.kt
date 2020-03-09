package ChatTheSpire.command

import ChatTheSpire.util.Spire
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CardCommand::class.java.name)

object CardCommand : Command {

    override fun perform(parameters: List<Int>) =
        execute(parameters = parameters, addAction = true)

    override fun canPerform(parameters: List<Int>): Boolean =
        execute(parameters = parameters, addAction = false)

    private fun execute(parameters: List<Int>, addAction: Boolean): Boolean {
        if (parameters.size !in 1..2) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val cardIndex = parameters[0]

        val card = Spire.hand?.getOrNull(cardIndex - 1)
        if (card == null) {
            logger.info("Invalid card index: {}", cardIndex)
            return false
        }

        val monster = if (parameters.size == 2) {
            val monsterIndex = parameters[1] - 1
            Spire.monsters?.getOrNull(monsterIndex)
        } else {
            null
        }

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

        if (addAction) {
            AbstractDungeon.actionManager.addToBottom(NewQueueCardAction(card, monster))
        }

        return true
    }
}
